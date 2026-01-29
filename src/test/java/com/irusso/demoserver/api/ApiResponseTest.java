package com.irusso.demoserver.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for ApiResponse.
 */
class ApiResponseTest {

    @Test
    void testSuccessWithData() {
        String data = "test data";
        ApiResponse<String> response = ApiResponse.success(data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    void testSuccessWithMessageAndData() {
        String message = "Custom success message";
        String data = "test data";
        ApiResponse<String> response = ApiResponse.success(message, data);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
    }

    @Test
    void testError() {
        String errorMessage = "Something went wrong";
        ApiResponse<String> response = ApiResponse.error(errorMessage);

        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getMessage()).isEqualTo(errorMessage);
        assertThat(response.getData()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        ApiResponse<Integer> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage("Test message");
        response.setData(42);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Test message");
        assertThat(response.getData()).isEqualTo(42);
    }

    @Test
    void testConstructorWithAllParameters() {
        ApiResponse<String> response = new ApiResponse<>(true, "Success", "data");

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Success");
        assertThat(response.getData()).isEqualTo("data");
    }
}

