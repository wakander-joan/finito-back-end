package com.management.finito.handler;

import com.management.finito.admin.AdminTelemetryService;
import com.management.finito.pessoa.domain.Pessoa;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class RestResponseEntityExceptionHandler {
	private final AdminTelemetryService telemetry;

	/** E-mail do usuário autenticado que disparou a requisição (null se anônimo/sistema). */
	private String emailDoUsuario() {
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getPrincipal() instanceof Pessoa p) return p.getEmail();
		} catch (Exception ignored) { /* sem usuário */ }
		return null;
	}

	@ExceptionHandler(APIException.class)
	public ResponseEntity<ErrorApiResponse> handlerGenericException(APIException ex, HttpServletRequest req) {
		if (ex.getStatusException() != null && ex.getStatusException().is5xxServerError()) {
			telemetry.registraErro(req.getMethod(), req.getRequestURI(), ex.getStatusException().value(), ex.getMessage(), emailDoUsuario());
		}
		return ex.buildErrorResponseEntity();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorApiResponse> handlerGenericException(Exception ex, HttpServletRequest req) {
		log.error("Exception: ", ex);
		telemetry.registraErro(req.getMethod(), req.getRequestURI(), 500, ex.getClass().getSimpleName() + ": " + ex.getMessage(), emailDoUsuario());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorApiResponse.builder().description("INTERNAL SERVER ERROR!")
						.message("POR FAVOR INFORME AO ADMINISTRADOR DO SISTEMA!").build());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
}
