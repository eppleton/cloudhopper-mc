/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    documentationSidebar: [
        {
            type: 'category',
            label: 'Getting started',
            collapsed: false,
            items: [
                'modules/maven-archetype',
                'modules/demo',
            ],
        },
        {
            type: 'category',
            label: 'Runtime API',
            collapsed: true,
            items: [
                'modules/deployment-config-api',
                {
                    type: 'link',
                    label: 'Java API',
                    href: 'pathname:///api/deployment-config-api/index.html',
                },
            ],
        },
        {
            type: 'category',
            label: 'Generator API',
            collapsed: true,
            items: [
                'modules/deployment-config-generator-api',
                {
                    type: 'link',
                    label: 'Java API',
                    href: 'pathname:///api/deployment-config-generator-api/index.html',
                },
            ],
        },
        {
            type: 'category',
            label: 'Template based Generic generator',
            collapsed: true,
            items: [
                'modules/deployment-config-generator-generic',
                {
                    type: 'link',
                    label: 'Java API',
                    href: 'pathname:///api/deployment-config-generator-generic/index.html',
                },
            ],
        },
        {
            type: 'category',
            label: 'Platform Integration',
            collapsed: true,
            items: [
                'platforms/generator-overview', // comparison doc

                {
                    type: 'category',
                    label: 'AWS',
                    collapsed: true,
                    items: [
                        'modules/generator-aws-terraform',
                        'modules/provider-aws',
                        {
                            type: 'link',
                            label: 'Java API',
                            href: 'pathname:///api/provider-aws/index.html',
                        },
                    ],
                },
                {
                    type: 'category',
                    label: 'Azure',
                    collapsed: true,
                    items: [
                        'modules/generator-azure-terraform',
                        'modules/provider-azure',
                        {
                            type: 'link',
                            label: 'Java API',
                            href: 'pathname:///api/provider-azure/index.html',
                        },
                    ],
                },
                {
                    type: 'category',
                    label: 'GCP',
                    collapsed: true,
                    items: [
                        'modules/generator-gcp-terraform',
                        'modules/provider-gcp',
                        {
                            type: 'link',
                            label: 'Java API',
                            href: 'pathname:///api/provider-gcp/index.html',
                        },
                    ],
                },
            ],
        }
    ],
};

export default sidebars;
