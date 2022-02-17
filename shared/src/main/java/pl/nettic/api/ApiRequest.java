package pl.nettic.api;

import javax.validation.constraints.NotBlank;

public record ApiRequest(@NotBlank(message = "base64str can't be blank") String base64str) {
}
