package sh4dow18.maitrofy_api
// Security Dependencies
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.crypto.SecretKey
// Security Constants Object that has all constants for Security Filters
object SecurityConstants {
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_ISSUER = "secure-api"
    const val TOKEN_AUDIENCE = "secure-app"
    const val TOKEN_LIFETIME: Long = 864000000
    const val TOKEN_PREFIX = "Bearer "
    const val APPLICATION_JSON = "application/json"
    const val UTF_8 = "UTF-8"
    val SECRET_KEY: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
}
// JWT Authentication Filter Class that is a Custom Security Filter that extends of
// "UsernamePasswordAuthenticationFilter" and would be used to JWT authentication
@Suppress("unused")
class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {
    // Variables Declaration
    private val authManager: AuthenticationManager
    // JWTAuthenticationFilter Constructor
    init {
        // Establishes the URL that will listen for this filter
        setFilterProcessesUrl("/api/login")
        authManager = authenticationManager
    }
    // Tag that allows to throw an Authentication Exception
    @Throws(AuthenticationException::class)
    // Function that works when a "User" tries to authenticate
    // This function receives the HTTP request and the HTTP response
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): Authentication {
        // Verifies if it is a POST Request, if not, throw an Authentication Service Exception
        if (request.method != "POST") {
            throw AuthenticationServiceException("Metodo de Autenticación no Soportado: ${request.method}")
        }
        // If it is a successful authentication, returns an "Authentication" object that
        // represents the "User" authenticated. If fails the authentication, throws an Exception
        return try {
            // "ObjectMapper" read and deserialize the Request HTTP body to
            // Transforms the Request to "UserLoginRequest"
            val userLoginInput: LoginRequest = ObjectMapper()
                .readValue(request.inputStream, LoginRequest::class.java)
            // Authentication Manager is used to authenticate the User with the
            // Username Password Authentication Token object that needs the email and
            // password of the "User". Also, it needs an Array List of all
            // privileges of this "User"
            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userLoginInput.email,
                    userLoginInput.password,
                    ArrayList())
            )
        }
        // If an Input/Output Exception occurs, it would be caught to throw a Runtime Exception
        catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }
    // Function that will create a JWT if the authentication success
    override fun successfulAuthentication(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain, authentication: Authentication,
    ) {
        // Creates an "ObjectMapper" that is used to serialize Java to JSON and JSON to Java
        val objectMapper = ObjectMapper()
        // Get the user id from database
        val userId = (authentication.principal as org.springframework.security.core.userdetails.User).username
        // Creates the JWT
        val token = Jwts.builder()
            .signWith(SecurityConstants.SECRET_KEY)
            .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
            .setIssuer(SecurityConstants.TOKEN_ISSUER)
            .setAudience(SecurityConstants.TOKEN_AUDIENCE)
            .setSubject(userId)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.TOKEN_LIFETIME))
            // Compact the JWT in a string
            .compact()
        // Add the JWT as a Header called "Authorization" for the HTTP Response.
        response.addHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token)
        // Generates an Outflow "PrintWriter" to write the response
        val out = response.writer
        response.contentType = SecurityConstants.APPLICATION_JSON
        response.characterEncoding = SecurityConstants.UTF_8
        // It serializes the main authentication object, that is the authenticated "User"
        out.print(objectMapper.writeValueAsString(authentication.principal))
        // It ensures the information in the HTTP Response
        out.flush()
    }
}
// Jwt Authorization Filter Class that is a Custom Security Filter that extends of
// "BasicAuthenticationFilter" and would be used to JWT authorization. This class only be used if
// the program would have an authorize "User"
@Suppress("unused")
class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) :
    BasicAuthenticationFilter(authenticationManager) {
    @Throws(IOException::class)
    // Override Function that would be executed with every HTTP request
    // Here is the JWT logical authorization
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // Returns the JWT of header "Authorization" in HTTP Request
        var authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        // Verifies if the token is null to send an Unauthorized error
        if (authorizationToken == null || !authorizationToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            SecurityContextHolder.clearContext()
            RestAuthenticationEntryPoint().commence(request, response, null)
            return
        }
        try {
            // Remove the Token Prefix
            authorizationToken = authorizationToken.replaceFirst(SecurityConstants.TOKEN_PREFIX.toRegex(), "")
            // Verifies if it is a valid token
            // It verifies the token signature key with the Secret Key in Security Constants
            // Then, extract the username that is the subject of body
            val userId: String = Jwts.parserBuilder()
                .setSigningKey(SecurityConstants.SECRET_KEY)
                .build()
                .parseClaimsJws(authorizationToken)
                .body
                .subject
            // Registers the username in a local variable for the actual thread
            LoggedUser.logIn(userId.toLong())
            // Establishes the Spring Security Context Authentication that is a
            // "UsernamePasswordAuthenticationToken" with the username extracted of the
            // JWT Token and an "emptyList" that are the "User" privileges
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userId, null, emptyList())
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
        }
        // This allows the Request to continue with the process for which it was created
        filterChain.doFilter(request, response)
    }

}
// Logged User Object to control the Actual Thread
object LoggedUser {
    // Variable Declaration to calls the local actual Thread
    private val userHolder = ThreadLocal<Long>()
    // Registers the username in a local variable for the actual thread
    fun logIn(user: Long) {
        userHolder.set(user)
    }
    // Get the username of the local variable of the actual thread
    fun get(): Long {
        return userHolder.get()
    }
}