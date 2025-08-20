package tech.jabari.common.exception;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.jabari.common.result.Result;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(500, e.getMessage());
    }

    @ExceptionHandler(ArithmeticException.class)
    public Result<Void> handleArithmeticException(ArithmeticException e) {
        return Result.fail(500, "算数异常");
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,  BindException.class})
    public Result<Void> handleValidException(Exception e) {
        String message = "参数校验失败";
        if (e instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) e).getBindingResult()
                    .getFieldError().getDefaultMessage();
        } else if (e instanceof BindException) {
            message = ((BindException) e).getBindingResult()
                    .getFieldError().getDefaultMessage();
        }
        return Result.fail(400, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        return Result.fail(400, e.getMessage());
    }



    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        System.out.println("--------参数类型不匹配！！！！！！！！！！！！！！");
        String error = "参数类型不匹配错误: " + ex.getName() + " 应为 " + ex.getRequiredType().getSimpleName() + " 但收到 " + ex.getValue();
        return Result.fail(400, error);
    }
}