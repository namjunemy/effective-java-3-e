package chap04.item15;

import java.util.List;

public interface WithdrawalService {
    String withdrawalMember(Member member);
    List<Member> getMembers();
    default String startProcess() {
        List<Member> members = getMembers();
        members.forEach(this::withdrawalMember);

        return "FINISHED";
    }
}
