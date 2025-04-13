/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    tutorialSidebar: [
        {
            type: 'category',
            label: 'Runtime API',
            collapsed: false,
            items: [
                'modules/deployment-config-api',
                {
                    type: 'link',
                    label: 'Java API',
                    href: 'pathname:///cloudhopper-mc/api/deployment-config-api/index.html',
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
                    href: 'pathname:///cloudhopper-mc/api/deployment-config-generator-api/index.html',
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
                    href: 'pathname:///cloudhopper-mc/api/deployment-config-generator-generic/index.html',
                },
            ],
        },
    ],
};

export default sidebars;
