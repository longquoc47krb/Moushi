package id.longquoc.messenger.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {
    private int status;
    private String message;
    private Object data;

    public ResponseObject(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
