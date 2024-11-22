package org.danpoong.zipcock_44.global.dto;

import lombok.Data;

public class KakaoDTO {

    @Data
    public static class OAuthToken{
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }

    @Data
    public static class KakaoProfile {
        private Long id;
        private String connected_at;
        private Properties properties;
        private KakaoAccount kakao_account;

        @Data
        public class Properties {
            private String nickname;
        }

        @Data
        public class KakaoAccount {
            private String email;
            private Boolean is_email_verified;
            private Boolean has_email;
            private Boolean profile_nickname_needs_agreement;
            private Boolean email_needs_agreement;
            private Boolean is_email_valid;
            private Profile profile;

            @Data
            public class Profile {
                private String nickname;
                private Boolean is_default_nickname;
            }
        }
    }


}
