<%--
  Created by IntelliJ IDEA.
  User: sanmaharjan
  Date: 11/15/2017
  Time: 2:06 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;
charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Search for Races</title>
</head>
<body>
<div class="body">
    <h1>Search for Races</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:form action="search" method="post" >
        <div class="dialog">
            <table>
                <tr class='prop'>
                    <td valign='top' class='name'>
                        <label for='city'>City:</label>
                    </td>
                    <td valign='top' class='value'>
                        <input type="text" maxlength='30' name='city'>
                    </input>
                    </td>
                    GET DYNAMIC | 57
                </tr>
            </table>
        </div>
        <div class="buttons">
        <input type="submit" value="Search"
        class="formbutton">
        </input>
        </div>
    </g:form>
</div>
</body>
</html>