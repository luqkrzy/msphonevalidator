package io.aeon.ocrparser;

import javax.validation.constraints.NotBlank;

public record ApiRequest(@NotBlank(message = "can't be blank") String type,
                         @NotBlank(message = "can't be blank") String base64str) {
}
