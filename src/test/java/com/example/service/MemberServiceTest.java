package com.example.service;

import com.example.jpashop.JpashopApplication;
import com.example.jpashop.domain.Member;
import com.example.jpashop.repository.MemberRepository;
import com.example.jpashop.service.MemberService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpashopApplication.class)
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입()throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveID = memberService.join(member);

        em.flush();
        //then
        Assert.assertEquals(member, memberRepository.findOne(saveID));

    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        try {
            memberService.join(member1);
            memberService.join(member2);
        }catch (IllegalStateException e){

            return;
        }
        //when

        //then
        fail("예외가 발생해야 한다");
    }

}