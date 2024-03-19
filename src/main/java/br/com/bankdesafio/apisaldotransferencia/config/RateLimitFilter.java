package br.com.bankdesafio.apisaldotransferencia.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.time.Duration;

@WebFilter(urlPatterns = "api/bacen/transacao/notificar")
public class RateLimitFilter implements Filter {

    private final Bucket bucket;

    public RateLimitFilter() {
        // Por exemplo, permitir 180000 requisições por minuto.
        Bandwidth limit = Bandwidth.classic(180000, Refill.greedy(180000, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            // Se o limite for excedido, retorna 429 Too Many Requests
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpResponse.getWriter().write("Rate limit exceeded. Try again later.");
        }
    }
}
