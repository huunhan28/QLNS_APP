package com.example.food.Domain.request;

public class RequestChangePassword{
    String username;
    String oldPassword;
    String newPassword;
    String confirmPassword;

    public RequestChangePassword(String username, String oldPassword, String newPassword, String confirmPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public boolean isValidOldPassword(){
        return oldPassword!=null && !oldPassword.equals("") && oldPassword.length()>=6;
    }

    public boolean isValidNewPassword(){
        return newPassword!=null && !newPassword.equals("") && newPassword.length()>=6;
    }

    public boolean isValidConfirmPassword(){
        return confirmPassword!=null && !confirmPassword.equals("") && confirmPassword.length()>=6 && confirmPassword.equals(newPassword);
    }

    public String getNewPassword() {
        return newPassword;
    }
}
