<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>My files</title></head>
<body>
    <h2>Files</h2>
    <table class="table table-success table-striped">
        <tbody>
        <c:forEach var="file" items="${files}">
            <tr>
                <td>${file.fileName}</td>
                <td>
                    <a href="DownloadFileServlet?filePath=${file.filePath.replace('\\', '/')}">Download</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>