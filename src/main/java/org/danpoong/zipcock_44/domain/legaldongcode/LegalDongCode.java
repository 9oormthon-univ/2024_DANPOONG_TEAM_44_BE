package org.danpoong.zipcock_44.domain.legaldongcode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.global.util.parse.LegalDongCodeParser;

import java.util.Map;

@Getter
@NoArgsConstructor
public class LegalDongCode {
    private static final Map<String, String> legalDongCodeMap = LegalDongCodeParser.parseLegalDongCode();

    public static String getCodeByAddress(String address) {
        return legalDongCodeMap.get(address);
    }
}
