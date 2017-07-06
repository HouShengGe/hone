package com.mc.app.hotel.common.facealignment.model;

/**
 * Created by gaofeng on 2017-02-10.
 */
public class FaceAlignmentResponse {
    private String status;

    private String msg;

    private String Similarity;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getSimilarity ()
    {
        return Similarity;
    }

    public void setSimilarity (String Similarity)
    {
        this.Similarity = Similarity;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", msg = "+msg+", Similarity = "+Similarity+"]";
    }
}
