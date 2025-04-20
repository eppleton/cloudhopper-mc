#!/bin/bash

set -e  # stop on error

# Colors
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo -e "${GREEN}Starting Cloudhopper Release...${NC}"

# 1. Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "Maven could not be found. Please install Maven first."
    exit 1
fi

# 2. Read current version
current_version=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
release_version=${current_version/-SNAPSHOT/}
next_snapshot_version=$(echo $release_version | awk -F. -v OFS=. '{$NF += 1; print $0 "-SNAPSHOT"}')

echo -e "${GREEN}Current version:${NC} $current_version"
echo -e "${GREEN}Release version:${NC} $release_version"
echo -e "${GREEN}Next snapshot version:${NC} $next_snapshot_version"

# 3. Confirmation
read -p "Do you want to release version ${release_version}? (y/n) " -n 1 -r
echo    # new line
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "Aborted."
    exit 1
fi

# 4. Set version to release version
echo -e "${GREEN}Setting release version...${NC}"
mvn versions:set -DnewVersion=${release_version}
mvn versions:commit

# 5. Commit and tag
git commit -am "Release version ${release_version}"
git tag -a "v${release_version}" -m "Release version ${release_version}"

# 6. Build and deploy
echo -e "${GREEN}Deploying release to OSSRH...${NC}"
mvn clean deploy -P release

# 7. Bump version to next snapshot version
echo -e "${GREEN}Preparing next development version...${NC}"
mvn versions:set -DnewVersion=${next_snapshot_version}
mvn versions:commit

git commit -am "Prepare next development iteration: ${next_snapshot_version}"

# 8. Push
git push origin main
git push origin --tags

echo -e "${GREEN}Release ${release_version} successfully deployed and pushed!${NC}"
