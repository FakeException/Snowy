package me.darkboy.utils;

public class UserDetails {

   private Long id;

   private String username;

   private String email;

   private String password;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUsername() {
      return this.username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getEmail() {
      return this.email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.intValue();
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else if (this.id == null) {
         return false;
      } else if (!(obj instanceof UserDetails other)) {
         return false;
      } else {
         return this.id.equals(other.id);
      }
   }
}