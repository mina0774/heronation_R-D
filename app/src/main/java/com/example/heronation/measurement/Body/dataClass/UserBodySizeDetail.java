package com.example.heronation.measurement.Body.dataClass;

import com.example.heronation.login_register.dataClass.BodyResponse;

import java.util.List;

public class UserBodySizeDetail {
    private List<BodyResponse> bodyResponses = null;
    private DmodelResponse dmodelResponse;
    private ImageDmodelResponse imageDmodelResponse;

    public List<BodyResponse> getBodyResponses() {
        return bodyResponses;
    }

    public void setBodyResponses(List<BodyResponse> bodyResponses) {
        this.bodyResponses = bodyResponses;
    }

    public DmodelResponse getDmodelResponse() {
        return dmodelResponse;
    }

    public void setDmodelResponse(DmodelResponse dmodelResponse) {
        this.dmodelResponse = dmodelResponse;
    }

    public ImageDmodelResponse getImageDmodelResponse() {
        return imageDmodelResponse;
    }

    public void setImageDmodelResponse(ImageDmodelResponse imageDmodelResponse) {
        this.imageDmodelResponse = imageDmodelResponse;
    }
}
