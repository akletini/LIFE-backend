package akletini.life.core.shared.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@SuperBuilder
public class HttpResponse {
    protected String timeStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected Map<?, ?> data;
}