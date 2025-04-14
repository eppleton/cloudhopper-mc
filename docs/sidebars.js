/** @type {import('@docusaurus/plugin-content-docs').SidebarsConfig} */
const sidebars = {
    documentationSidebar: [
        {
            type: 'category',
            label: 'Runtime API',
            collapsed: false,
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
            label: 'The Demo Project',
            collapsed: true,
            items: [
                'modules/demo',
            ],
        },
    ],
};

export default sidebars;
