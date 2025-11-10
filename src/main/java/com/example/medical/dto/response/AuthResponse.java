package com.example.medical.dto.response;

public class AuthResponse {
    private boolean success;
    private String token;
    private String message;
    private UserInfo userInfo;

    public AuthResponse() {}

    public AuthResponse(boolean success, String token, String message, UserInfo userInfo) {
        this.success = success;
        this.token = token;
        this.message = message;
        this.userInfo = userInfo;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public UserInfo getUserInfo() { return userInfo; }
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }

    public static class UserInfo {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private String role;

        public UserInfo() {}

        public UserInfo(Long id, String username, String fullName, String email, String role) {
            this.id = id;
            this.username = username;
            this.fullName = fullName;
            this.email = email;
            this.role = role;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}