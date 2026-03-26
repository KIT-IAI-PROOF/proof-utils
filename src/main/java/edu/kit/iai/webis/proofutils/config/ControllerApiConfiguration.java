/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import edu.kit.iai.webis.ApiClient;
import edu.kit.iai.webis.proofapi.AttachmentControllerApi;
import edu.kit.iai.webis.proofapi.BlockControllerApi;
import edu.kit.iai.webis.proofapi.ExecutionControllerApi;
import edu.kit.iai.webis.proofapi.ProgramControllerApi;
import edu.kit.iai.webis.proofapi.TemplateControllerApi;
import edu.kit.iai.webis.proofapi.WorkflowControllerApi;
import edu.kit.iai.webis.proofutils.model.ProcessEnvironment;

@Configuration
public class ControllerApiConfiguration {

	/**
	 * set the base path (to the port number set in proof-environment/docker/docker-compose.yaml)
	 */
    @Value("${proof.config.basePath:http://localhost:8100}")
    private String basePath;

    /**
     * set the processing environment to decide whether a bas path should be set
     */
    @Value("${proof.config.process.environment:DOCKER}")
    private String processEnvironment;

	@Bean(name = "workflowControllerApi", value = "workflowControllerApi")
	WorkflowControllerApi workflowControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
		return new WorkflowControllerApi(this.createApiClient(webClient));
	}

	@Bean(name = "blockControllerApi", value = "blockControllerApi")
	BlockControllerApi blockControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
		return new BlockControllerApi(this.createApiClient(webClient));
	}

	@Bean(name = "programControllerApi", value = "programControllerApi")
	ProgramControllerApi programControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
		return new ProgramControllerApi(this.createApiClient(webClient));
	}

	@Bean(name = "attachmentControllerApi", value = "attachmentControllerApi")
	AttachmentControllerApi attachmentControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
		return new AttachmentControllerApi(this.createApiClient(webClient));
	}

	@Bean(name = "templateControllerApi", value = "templateControllerApi")
	TemplateControllerApi templateControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
		return new TemplateControllerApi(this.createApiClient(webClient));
	}

    @Bean(name = "executionControllerApi", value = "executionControllerApi")
    ExecutionControllerApi executionControllerApi(@Qualifier("defaultWebClient") WebClient webClient) {
        return new ExecutionControllerApi(this.createApiClient(webClient));
    }

    private ApiClient createApiClient(WebClient webClient) {
    	final ApiClient apiClient = new ApiClient(webClient);
    	if( ProcessEnvironment.DOCKER.name().equalsIgnoreCase(this.processEnvironment)) {
    		apiClient.setBasePath(this.basePath);
    	}
    	return apiClient;
    }
}
