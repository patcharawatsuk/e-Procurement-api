package eprocurementapi.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import eprocurementapi.util.Constant;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ServiceResult {

    @JsonFormat(timezone = Constant.TimeZoneIds.ASIA_BANGKOK, pattern = Constant.DATETIME_FORMAT_ZONE)
    private Date timestamp;
    private Integer status;
    private Object data;

    public ServiceResult() {
        this.setStatus(HttpStatus.OK.value());
        this.setTimestamp(new Date());
    }

    public ServiceResult createResponseData(Object data) {
        this.setTimestamp(new Date());
        this.setData(data);
        return this;
    }
}
