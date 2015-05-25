<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:fo="http://www.w3.org/1999/XSL/Format"
        version="1.0">

    <xsl:output method="xml" indent="yes"/>

    <xsl:template match="/">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="all">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="all">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block margin-left="20mm" margin-top="20mm" margin-bottom="5mm" font-size="16pt"
                              font-weight="bold">
                        Projectname
                        <xsl:value-of select="project/@name"/>
                    </fo:block>
                    <fo:block margin-left="20mm" margin-right="20mm">
                        <fo:table>
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
                    <xsl:for-each select="project/qualityAttributes/qualityAttribute">
                        <fo:block margin-left="20mm" margin-right="20mm">
                            <xsl:value-of select="@description"/>
                        </fo:block>
                    </xsl:for-each>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <xsl:attribute-set name="table.cell.padding">
        <xsl:attribute name="padding-left">0pt</xsl:attribute>
        <xsl:attribute name="padding-right">0pt</xsl:attribute>
        <xsl:attribute name="padding-top">0pt</xsl:attribute>
        <xsl:attribute name="padding-bottom">0pt</xsl:attribute>
    </xsl:attribute-set>

</xsl:stylesheet>