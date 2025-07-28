package br.com.EasyTable.Shared.Models;

import br.com.EasyTable.Shared.Handlers.HandlerResponseWithResult;
import br.com.EasyTable.Shared.Helpers.FileContentResult;
import br.com.EasyTable.Shared.Helpers.TrimmingJsonSerializer;
import br.com.EasyTable.Shared.Properties.MessageResources;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

interface IResponseEntityConverter {
    ResponseEntity<?> convert(HandlerResponseWithResult<?> response, boolean withContentOnSuccess);
}

@Component
@Slf4j
public class ResponseEntityConverterImpl implements  IResponseEntityConverter {
    private static final Logger logger = LoggerFactory.getLogger(ResponseEntityConverterImpl.class);

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .registerModule(new JavaTimeModule())
            .registerModule(new SimpleModule()
                    .addSerializer(String.class, new TrimmingJsonSerializer()))
            .configure(SerializationFeature.INDENT_OUTPUT, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @Override
    public ResponseEntity<?> convert(HandlerResponseWithResult<?> response, boolean withContentOnSuccess) {
        if (response == null) {
            return buildError(List.of(new ErrorMessage("000", MessageResources.get(("error.responseEntity_convert_error")))),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (response.getMessages() != null && !response.getMessages().isEmpty()) {
            return buildError(response.getMessages(), HttpStatus.valueOf(response.getStatusCode()));
        }

        if (response.isSuccess()) {
            if (withContentOnSuccess) {
                return buildResult(response.getResult(), HttpStatus.valueOf(response.getStatusCode()));
            }
            return ResponseEntity.noContent().build();
        }

        if (response.getResult() != null) {
            return buildResult(response.getResult(), HttpStatus.valueOf(response.getStatusCode()));
        }

        return buildResult(response.getResult(), HttpStatus.valueOf(response.getStatusCode()));
    }

    private static ResponseEntity<?> buildError(Object data, HttpStatus statusCode) {
        logger.error("[ERROR] {}", data);
        return toJsonResult(data, statusCode);
    }

    private static ResponseEntity<?> buildResult(Object data, HttpStatus statusCode) {
        if (data instanceof ByteArrayResource result) {
            return ResponseEntity.status(statusCode)
                    .header("Content-Type", "application/octet-stream")
                    .body(result);
        }

        if (data instanceof FileContentResult fileContentResult) {
            return ResponseEntity.status(statusCode)
                    .header("Content-Type", fileContentResult.getContentType())
                    .header("Content-Disposition", "attachment; filename=\"" + fileContentResult.getFileName() + "\"")
                    .body(new ByteArrayResource(fileContentResult.getFileData()));
        }
        return toJsonResult(data, statusCode);
    }

    private static ResponseEntity<?> toJsonResult(Object data, HttpStatus statusCode) {
        try {
            String json = objectMapper.writeValueAsString(data);
            return ResponseEntity.status(statusCode).body(json);
        } catch (Exception e) {
            logger.error("Error converting object to JSON", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
