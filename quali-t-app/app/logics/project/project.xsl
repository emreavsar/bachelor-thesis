<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:fo="http://www.w3.org/1999/XSL/Format"
        version="1.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="all"
                                       margin-top="2.5cm"
                                       margin-bottom="0.5cm"
                                       margin-left="2.5cm"
                                       margin-right="2.5cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="all">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block margin-bottom="5mm" font-size="16pt"
                              font-weight="bold">
                        Project
                        <xsl:value-of select="project/@name"/>
                    </fo:block>
                    <fo:block text-align="left">
                        <fo:table padding="0">
                            <fo:table-body>
                                <fo:table-row>
                                    <fo:table-cell font-weight="bold">
                                        <fo:block>Jira-Key</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="project/@jiraKey"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                                <fo:table-row>
                                    <fo:table-cell font-weight="bold">
                                        <fo:block>Customer</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell>
                                        <fo:block>
                                            <xsl:value-of select="project/projectCustomer/@name"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block margin-top="5mm" font-size="12pt"
                              font-weight="bold">
                        Quality Attributes
                    </fo:block>
                    <xsl:for-each select="project/qualityAttributes/qualityAttribute">
                        <fo:block margin-top="2mm">
                            &#9733;
                            <xsl:value-of select="@description"/>
                        </fo:block>
                    </xsl:for-each>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>