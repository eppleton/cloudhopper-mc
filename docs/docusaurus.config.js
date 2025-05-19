import {themes as prismThemes} from 'prism-react-renderer';

// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

/** @type {import('@docusaurus/types').Config} */
const config = {
    title: 'Cloudhopper mc',
    tagline: 'Hop on now...',
    favicon: 'img/favicon.ico',

    // Set the production url of your site here
    url: 'https://eppleton.github.io',
    // Set the /<baseUrl>/ pathname under which your site is served
    // For GitHub pages deployment, it is often '/<projectName>/'
    baseUrl: '/cloudhopper-mc/',

    // GitHub pages deployment config.
    // If you aren't using GitHub pages, you don't need these.
    organizationName: 'eppleton', // Usually your GitHub org/user name.
    projectName: 'cloudhopper-mc', // Usually your repo name.

    onBrokenLinks: 'throw',
    onBrokenMarkdownLinks: 'warn',

    // Even if you don't use internationalization, you can use this field to set
    // useful metadata like html lang. For example, if your site is Chinese, you
    // may want to replace "en" with "zh-Hans".
    i18n: {
        defaultLocale: 'en',
        locales: ['en'],
    },

    // Enable Mermaid support
    markdown: {
        mermaid: true,
    },

    // Pull in the Mermaid theme
    themes: [
        '@docusaurus/theme-mermaid',
    ],

    presets: [
        [
            'classic',
            /** @type {import('@docusaurus/preset-classic').Options} */
            ({
                docs: {
                    sidebarPath: './sidebars.js',
                },
//        blog: {
//          showReadingTime: true,
//          feedOptions: {
//            type: ['rss', 'atom'],
//            xslt: true,
//          },
//          // Useful options to enforce blogging best practices
//          onInlineTags: 'warn',
//          onInlineAuthors: 'warn',
//          onUntruncatedBlogPosts: 'warn',
//        },
                theme: {
                    customCss: './src/css/custom.css',
                },
            }),
        ],
    ],

    themeConfig:
            /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
                    ({
                        // Replace with your project's social card
                        image: 'img/docusaurus-social-card.jpg',
                        navbar: {
                            title: 'Cloudhopper mc',
                            logo: {
                                alt: 'Cloudhopper mc Logo',
                                src: 'img/logo.svg',
                            },
                            items: [
                                {
                                    type: 'docSidebar',
                                    sidebarId: 'documentationSidebar',
                                    position: 'left',
                                    label: 'Documentation',
                                },
                                {
                                    href: 'https://github.com/eppleton/cloudhopper-mc',
                                    label: 'GitHub',
                                    position: 'right',
                                },
                            ],
                        },
                        footer: {
                            style: 'dark',
                            links: [
                                {
                                    title: 'Docs',
                                    items: [
                                        {
                                            label: 'Tutorial',
                                            to: '/docs/intro',
                                        },
                                    ],
                                },
                                {
                                    title: 'Community',
                                    items: [
                                        {
                                            label: 'Stack Overflow',
                                            href: 'https://stackoverflow.com/questions/tagged/cloudhopper',
                                        },
                                    ],
                                },
                                {
                                    title: 'More',
                                    items: [
//              {
//                label: 'Blog',
//                to: '/blog',
//              },
                                        {
                                            label: 'GitHub',
                                            href: 'https://github.com/eppleton/cloudhopper-mc',
                                        },
                                    ],
                                },
                            ],
                            copyright: `Copyright © ${new Date().getFullYear()} Eppleton It Consulting. Built with Docusaurus.`,
                        },
                        prism: {
                            theme: prismThemes.github,
                            darkTheme: prismThemes.dracula,
                        },
                        mermaid: {
                            theme: {light: 'neutral', dark: 'forest'},
                        },
                    }),
        };

        export default config;
