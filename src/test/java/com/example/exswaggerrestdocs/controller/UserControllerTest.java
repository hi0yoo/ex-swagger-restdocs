package com.example.exswaggerrestdocs.controller;


import com.epages.restdocs.apispec.*;
import com.example.exswaggerrestdocs.common.ApiResponse;
import com.example.exswaggerrestdocs.controller.request.UserAddRequest;
import com.example.exswaggerrestdocs.entity.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // 요청 성공시 공통 응답 Spec 부분 추출
    FieldDescriptor[] successResponseFields = {PayloadDocumentation.fieldWithPath("responseTime").type(JsonFieldType.STRING).description("응답 시간"),
            PayloadDocumentation.subsectionWithPath("errorMessage").type(JsonFieldType.STRING).description("오류 메시지").optional(),
            PayloadDocumentation.subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터").optional()};

    // 요청 데이터 검증 실패시 공통 응답 Spec 부분 추출
    FieldDescriptor[] validFailResponseFields = {PayloadDocumentation.fieldWithPath("responseTime").type(JsonFieldType.STRING).description("응답 시간"),
            PayloadDocumentation.subsectionWithPath("errorMessage").type(JsonFieldType.STRING).description("오류 메시지"),
            PayloadDocumentation.subsectionWithPath("data").type(JsonFieldType.OBJECT).description("오류 필드")};

    // 비즈니스 예외 발생시 공통 응답 Spec 부분 추출
    FieldDescriptor[] commonExceptionResponseFields = {PayloadDocumentation.fieldWithPath("responseTime").type(JsonFieldType.STRING).description("응답 시간"),
            PayloadDocumentation.subsectionWithPath("errorMessage").type(JsonFieldType.STRING).description("오류 메시지"),
            PayloadDocumentation.subsectionWithPath("data").type(JsonFieldType.OBJECT).description("오류 데이터").optional()};

    @Nested
    @DisplayName("유저 생성")
    class userAdd {

        @Test
        @DisplayName("유저 생성 성공")
        public void success() throws Exception {
            ConstrainedFields requestFields = new ConstrainedFields(UserAddRequest.class);
            ConstrainedFields responseFields = new ConstrainedFields(ApiResponse.class);

            // given
            UserAddRequest userAddRequest = new UserAddRequest("user1", 25, Gender.MEN);

            // when
            ResultActions perform = mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(objectMapper.writeValueAsString(userAddRequest))
            );

            // then
            perform.andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").isNotEmpty());

            // docs
            perform.andDo(
                    MockMvcRestDocumentationWrapper.document(
                            "{class-name}/{method-name}",
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            ResourceDocumentation.resource(
                                    ResourceSnippetParameters.builder()
                                            .description("유저 생성")
                                            .requestFields(
                                                    requestFields.withPath("name").description("이름"),
                                                    requestFields.withPath("age").description("나이"),
                                                    requestFields.withPath("gender").description("성별")
                                            )
                                            .responseFields(
                                                    new FieldDescriptors(successResponseFields).andWithPrefix(
                                                            "data.",
                                                            responseFields.withPath("userId").description("생성된 유저 식별값")
                                                    )
                                            )
                                            .build()
                            )
                    )
            );
        }

        @Test
        @DisplayName("요청 데이터 검증 실패")
        public void validFail() throws Exception {
            ConstrainedFields responseFields = new ConstrainedFields(ApiResponse.class);

            // given
            UserAddRequest userAddRequest = new UserAddRequest(null, 10, null);

            // when
            ResultActions perform = mockMvc.perform(
                    RestDocumentationRequestBuilders.post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(objectMapper.writeValueAsString(userAddRequest))
            );

            // then
            perform.andExpect(MockMvcResultMatchers.status().isBadRequest());

            // docs
            perform.andDo(
                    MockMvcRestDocumentationWrapper.document(
                            "{class-name}/{method-name}",
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            ResourceDocumentation.resource(
                                    ResourceSnippetParameters.builder()
                                            .responseFields(
                                                    new FieldDescriptors(validFailResponseFields).andWithPrefix(
                                                            "data.",
                                                            responseFields.withPath("name").description("name 필드 검증 오류 메시지 리스트").optional(),
                                                            responseFields.withPath("age").description("age 필드 검증 오류 메시지 리스트").optional()
                                                    )
                                            )
                                            .build()
                            )
                    )
            );
        }
    }
}
