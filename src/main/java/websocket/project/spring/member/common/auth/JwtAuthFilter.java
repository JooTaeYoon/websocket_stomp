package websocket.project.spring.member.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String token = httpServletRequest.getHeader("Authorization");
        try {
            if (token != null) {

                if (!token.substring(0, 7).equals(("Bearer "))) {
                    throw new AuthenticationException(("Bearer 형식이 아닙니다."));
                }
                String jwtToken = token.substring(7);
                Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(jwtToken).getBody(); // 토큰 검증 및 claims 추출

                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                System.out.println("if");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            System.out.println("if 밖");
            filterChain.doFilter(request, response);  // filterChain 사용 목적 -> 다시 필터체인으로 돌아 갈때, filterChain.doFilter를 사용 함
//        request 안에 토큰이 들어가 있음
        } catch (Exception e) {
            e.printStackTrace();
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write("invalid token !!!");
        }

    }
}
