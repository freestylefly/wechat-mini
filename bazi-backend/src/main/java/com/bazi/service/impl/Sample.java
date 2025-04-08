package com.bazi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.volcengine.ark.runtime.model.completion.chat.*;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest.*;
import com.volcengine.ark.runtime.service.ArkService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

/*
# pom.xml
<dependency>
  <groupId>com.volcengine</groupId>
  <artifactId>volcengine-java-sdk-ark-runtime</artifactId>
  // 替换正式版本号
  <version>LATEST</version>
</dependency>
*/

public class Sample {

    /**
     * Authentication 1.If you authorize your endpoint using an API key, you can set your api key to
     * environment variable "ARK_API_KEY" String apiKey = System.getenv("ARK_API_KEY"); ArkService
     * service = new ArkService(apiKey); Note: If you use an API key, this API key will not be
     * refreshed. To prevent the API from expiring and failing after some time, choose an API key
     * with no expiration date.
     *
     * <p>2.If you authorize your endpoint with Volcengine Identity and Access Management（IAM), set
     * your api key to environment variable "VOLC_ACCESSKEY", "VOLC_SECRETKEY" String ak =
     * System.getenv("VOLC_ACCESSKEY"); String sk = System.getenv("VOLC_SECRETKEY"); ArkService
     * service = new ArkService(ak, sk); To get your ak&sk, please refer to this
     * document(https://www.volcengine.com/docs/6291/65568) For more information，please check this
     * document（https://www.volcengine.com/docs/82379/1263279）
     */
//    static String apiKey = System.getenv("e52c3178-232f-431c-a651-97e4737e8676");
    static String apiKey = "e52c3178-232f-431c-a651-97e4737e8676";

    static ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
    static Dispatcher dispatcher = new Dispatcher();
    static ArkService service =
            ArkService.builder()
                    .dispatcher(dispatcher)
                    .connectionPool(connectionPool)
                    .apiKey(apiKey)
                    .build();

    public static void main(String[] args) throws JsonProcessingException {

        List<ChatMessage> messagesForReqList = new ArrayList<>();

        ChatMessage elementForMessagesForReqList0 =
                ChatMessage.builder().role(ChatMessageRole.USER).content("推理模型与非推理模型区别").build();

        ChatMessage elementForMessagesForReqList1 =
                ChatMessage.builder()
                        .role(ChatMessageRole.ASSISTANT)
                        .content(
                                "推理模型主要依靠逻辑、规则或概率等进行分析、推导和判断以得出结论或决策，非推理模型则是通过模式识别、统计分析或模拟等方式来实现数据描述、分类、聚类或生成等任务而不依赖显式逻辑推理。")
                        .build();

        ChatMessage elementForMessagesForReqList2 =
                ChatMessage.builder()
                        .role(ChatMessageRole.USER)
                        .content("我要有研究推理模型与非推理模型区别的课题，怎么体现我的专业性")
                        .build();
        messagesForReqList.add(elementForMessagesForReqList0);
        messagesForReqList.add(elementForMessagesForReqList1);
        messagesForReqList.add(elementForMessagesForReqList2);

        ChatCompletionRequestStreamOptions streamOptionsForReq =
                new ChatCompletionRequestStreamOptions(true);

        ChatCompletionRequest req =
                ChatCompletionRequest.builder()
                        .model("deepseek-r1-250120")
                        .messages(messagesForReqList)
                        .stream(false)
                        .streamOptions(streamOptionsForReq)
                        .logprobs(false)
                        .build();

        service.createChatCompletion(req)
                .getChoices()
                .forEach(choice -> System.out.println(choice.getMessage().getContent()));
        // shutdown service after all requests is finished
        service.shutdownExecutor();
    }
}

