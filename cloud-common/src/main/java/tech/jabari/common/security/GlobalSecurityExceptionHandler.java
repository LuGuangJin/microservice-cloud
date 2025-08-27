// cloud-common/src/main/java/tech/jabari/common/security/GlobalSecurityExceptionHandler.java
package tech.jabari.common.security;

/**
 * 全局安全异常处理器
 */
//@RestControllerAdvice
//public class GlobalSecurityExceptionHandler {
//
//    /**
//     * 访问拒绝异常处理器（授权异常处理器）
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.FORBIDDEN.value());
//        response.put("error", "Forbidden");
//        response.put("message", "Access denied: " + ex.getMessage());
//        response.put("timestamp", System.currentTimeMillis());
//
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
//    }
//
//    /**
//     * 认证异常处理器
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException ex) {
//        Map<String, Object> response = new HashMap<>();
//        response.put("status", HttpStatus.UNAUTHORIZED.value());
//        response.put("error", "Unauthorized");
//        response.put("message", "Authentication failed: " + ex.getMessage());
//        response.put("timestamp", System.currentTimeMillis());
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
//}