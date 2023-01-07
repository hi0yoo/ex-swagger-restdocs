package com.example.exswaggerrestdocs.controller;


import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
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

    @Nested
    @DisplayName("유저 생성")
    class userAdd {

        @Test
        @DisplayName("유저 생성 성공")
        public void createUser() throws Exception {
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
                            MockMvcRestDocumentationWrapper.resourceDetails().description("유저 생성 성공"),
                            Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                            Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                            PayloadDocumentation.requestFields(
                                    Attributes.attributes(Attributes.key("title").value("요청 필드")),
                                    PayloadDocumentation.fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                    PayloadDocumentation.fieldWithPath("age").type(JsonFieldType.NUMBER).description("나이"),
                                    PayloadDocumentation.fieldWithPath("gender").type(JsonFieldType.STRING).description("성별")
                            ),
                            PayloadDocumentation.responseFields(
                                    Attributes.attributes(Attributes.key("title").value("응답 필드")),
                                    PayloadDocumentation.fieldWithPath("data.userId").type(JsonFieldType.NUMBER).description("생성된 유저 식별값"),
                                    PayloadDocumentation.fieldWithPath("responseTime").ignored()
                            )
                    )
            );
        }
    }
}
