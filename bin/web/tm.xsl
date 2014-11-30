<?xml version="1.0" ?> 
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="turing-machine">

<html> <!--xmlns="http://www.w3.org/1999/xhtml"-->
<head>
<title>Untitled Document</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>

<body>
<p>
<img src="images/vtlogosm.gif" />
</p>
<div align="center">
<!-- Round Border -->
<table width="90%" height="90%" border="0" cellspacing="0" cellpadding="0">
  <tr>
      <td height="39"><img src="images/topleft.gif" width="39" height="39" /></td>
      <td><img src="images/top.gif" width="100%" height="39" /></td>
      <td height="39"><img src="images/topright.gif" width="39" height="39" /></td>
  </tr>
  <tr>
      <td><img src="images/left.gif" width="39" height="100%" /></td>
    <td>
	<!-- Shadow Border -->
	<div align="center">
		<table width="70%">
			<tr>
				<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					  <tr>
								
							<td height="10"><img src="images/s_toplft.gif" width="10" height="10" /></td>
							<td><img src="images/s_top.gif" width="100%" height="10" /></td>
							<td height="10"><img src="images/s_toprht.gif" width="10" height="10" /></td>
					  </tr>
					  <tr>
							<td><img src="images/s_left.gif" width="10" height="100%" /></td>
						<td><div align="center"><font size="5"><xsl:value-of select="name" /></font></div></td>
							<td><img src="images/s_rht.gif" width="10" height="100%" /></td>
					  </tr>
					  <tr>
							<td height="10" width="10"><img src="images/s_btmlft.gif" width="10" height="10" /></td>
							<td><img src="images/s_btm.gif" width="100%" height="10" /></td>
							<td width="10"><img src="images/s_btmrht.gif" width="10" height="10" /></td>
					  </tr>
					</table>
				</td>
			</tr>
			<tr>
			<td>
			<font size="4"><p>
			<xsl:value-of select="description" /></p>
			<p>
			<i>Q</i> =
			<p>{ <xsl:for-each select="states/state"> <xsl:value-of select="name" /><xsl:if test="position() != last()" >, </xsl:if> </xsl:for-each> }</p>
			</p>
			<p>
				<script language="JavaScript"> 
				document.write("&amp;Sigma");
				</script> =
				<p>{ <xsl:for-each select="alphabet/symbol">
				<script language="JavaScript"> 
				document.write("&amp;#x");
				</script>
				<xsl:choose>
					<xsl:when test=". != '22B3'">
						<xsl:value-of select="." />
					</xsl:when>
					<xsl:otherwise>22B2</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="position() != last()" >, </xsl:if> </xsl:for-each> }</p>
			</p>
			<p><script language="JavaScript"> 
				document.write("&amp;delta");
				</script> =
			</p>
			</font>
			
			<!-- transition table -->
			<p>
			<div align="left">
			<table style="border-collapse:collapse;" cellspacing="0" width="70%">
				
				<xsl:for-each select="transitions/transition">
				<tr>
					<td width="25%" style="border-top-width:1; border-right-width:1; border-bottom-width:1; border-left-width:1; border-color:black; border-top-style:solid; border-right-style:solid; border-bottom-style:solid; border-left-style:solid;">
						<div align="center">
							<xsl:value-of select="current-state" />
						</div>
					</td>
					<td width="25%" style="border-top-width:1; border-right-width:2	; border-bottom-width:1; border-left-width:1; border-color:black; border-top-style:solid; border-right-style:double; border-bottom-style:solid; border-left-style:solid;">
						<div align="center">
							<script language="JavaScript"> 
							document.write("&amp;#x");
							</script>
				<xsl:choose>
					<xsl:when test="current-symbol != '22B3'">
						<xsl:value-of select="current-symbol" />
					</xsl:when>
					<xsl:otherwise>22B2</xsl:otherwise>
				</xsl:choose>
						</div>
					</td>
					<td width="25%" style="border-top-width:1; border-right-width:1; border-bottom-width:1; border-left-width:1; border-color:black; border-top-style:solid; border-right-style:solid; border-bottom-style:solid; border-left-style:solid;">
						<div align="center">
							<xsl:value-of select="next-state" />
						</div>
					</td>
					<td width="25%" style="border-top-width:1; border-right-width:1; border-bottom-width:1; border-left-width:1; border-color:black; border-top-style:solid; border-right-style:solid; border-bottom-style:solid; border-left-style:solid;">
						<div align="center">
							<script language="JavaScript"> 
							document.write("&amp;#x");
							</script>
							<xsl:value-of select="task" />
						</div>
					</td>
				</tr>
				</xsl:for-each>
   			</table>
			</div>
			</p>

			</td>
			</tr>
		</table>
	</div>
	<br /></td>
      <td><img src="images/right.gif" width="39" height="100%" /></td>
  </tr>
  <tr>
      <td height="39" width="39"><img src="images/bottomleft.gif" width="39" height="39" /></td>
      <td><img src="images/bottom.gif" width="100%" height="39" /></td>
      <td width="39"><img src="images/bottomright.gif" width="39" height="39" /></td>
  </tr>
</table>

</div>
</body>
</html>
</xsl:template>
</xsl:stylesheet>