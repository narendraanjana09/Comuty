package com.nsa.comuty.home.ui.posts.models;

import java.util.List;

public class PostModel {
    private String postId;
    private String userName;
    private String userId;
    private String userImage;
    private String dateTime;
    private List<Like> likes;
    private List<Comment> comments;
    private String postMessage;
    private List<String> imagesList;

    public PostModel() {
    }

    public PostModel(String postId, String userName, String userId, String userImage, String dateTime, List<Like> likes, List<Comment> comments, String postMessage, List<String> imagesList) {
        this.postId = postId;
        this.userName = userName;
        this.userId = userId;
        this.userImage = userImage;
        this.dateTime = dateTime;
        this.likes = likes;
        this.comments = comments;
        this.postMessage = postMessage;
        this.imagesList = imagesList;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public class Like {
        private String userId;
        private String userName;
        private String userImage;
        private String dateTime;

        public Like() {
        }

        public Like(String userId, String userName, String userImage, String dateTime) {
            this.userId = userId;
            this.userName = userName;
            this.userImage = userImage;
            this.dateTime = dateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }
    public class Comment{
        private String userId;
        private String userName;
        private String userImage;
        private String dateTime;
        private String comment;

        public Comment() {
        }

        public Comment(String userId, String userName, String userImage, String dateTime, String comment) {
            this.userId = userId;
            this.userName = userName;
            this.userImage = userImage;
            this.dateTime = dateTime;
            this.comment = comment;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}

