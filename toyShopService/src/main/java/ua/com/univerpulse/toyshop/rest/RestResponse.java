package ua.com.univerpulse.toyshop.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class RestResponse {
    private Integer responseCode;
    private ObjectNode json;
}

