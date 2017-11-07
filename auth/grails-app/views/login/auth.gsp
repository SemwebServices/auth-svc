<%@ page import="org.springframework.security.web.savedrequest.SavedRequest" %>
<%@ page import="org.springframework.security.web.savedrequest.HttpSessionRequestCache" %>

<g:set var='securityConfig' value='${applicationContext.springSecurityService.securityConfig}'/>
<html>
<head>
  <meta name="layout" content="main"/>
	<s2ui:title messageCode='spring.security.ui.login.title'/>
</head>
<body>

  <div class="container">
    <div class="row">
      <div class="col-md-6">
        		<g:form controller="login" action="authenticate" method="POST" class="cssform" autocomplete="off">
        			<div class="sign-in">
        				<h2><g:message code='spring.security.ui.login.signin'/></h2>

        				<table>
        					<tr>
        						<td><label for="username"><g:message code='spring.security.ui.login.username'/></label></td>
        						<td><input type="text" name="${securityConfig.apf.usernameParameter}" id="username" class='formLogin' size="20"/></td>
        					</tr>
        					<tr>
        						<td><label for="password"><g:message code='spring.security.ui.login.password'/></label></td>
        						<td><input type="password" name="${securityConfig.apf.passwordParameter}" id="password" class="formLogin" size="20"/></td>
        					</tr>
        					<tr>
        						<td colspan='2'>
        							<label for='remember_me'><g:message code='spring.security.ui.login.rememberme'/></label>
        							<input type="checkbox" class="checkbox" name="${securityConfig.rememberMe.parameter}" id="remember_me" checked="checked"/>
                      <button type="submit">Login</button>
        						</td>
        					</tr>
        				</table>
        			</div>
        		</g:form>
      </div>
      <div class="col-md-6">
        <div class="sign-in">
          <h2><g:message code='spring.security.ui.login.oauthsignin'/></h2>
          <table>
            <tr><td>No oauth providers available currently</td></tr>
          </table>
      </div>
    </div>
  </div>

  params:
  ${params}
  saved req:
  <%
    def r = new HttpSessionRequestCache().getRequest(request, response)
  %>
  ${r}
</body>
</html>
