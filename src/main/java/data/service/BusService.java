package data.service;

import data.object.dto.BusDto;
import data.object.dto.BusResponseDto;
import data.object.entity.BusEntity;
import data.object.entity.MemberEntity;
import data.repository.BusRepository;
import data.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Slf4j
public class BusService {

    private final BusRepository busRepository;
    private final MemberRepository memberRepository;

    public BusService(BusRepository busRepository, MemberRepository memberRepository) {
        this.busRepository = busRepository;
        this.memberRepository = memberRepository;
    }

    public BusResponseDto getAllBuses(HttpServletRequest request) {
        HttpSession session = request.getSession();

        log.info("BusService - GetAllBuses Session ID: " + session.getId());

        Long memberId = (Long)session.getAttribute("memberId");

        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow();
        List<BusEntity> busEntities = busRepository.findAllByMember_MemberId(memberId);
        List<BusDto> busDtoList = new ArrayList<>();

        for(BusEntity bus : busEntities) {
            busDtoList.add(this.EntityToDto(bus));
        }
        return new BusResponseDto(busDtoList,memberId.toString(),member.getCompanyName());
    }

    public void addBusRoute(BusDto busDto, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Long memberId = (Long)session.getAttribute("memberId");
        System.out.println( "서비스에서 memberID" + memberId);
        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow();
        System.out.println( "버스 이름 : " + busDto.getName()+ ", 등록시킨 회원 번호 : " + member.getMemberId());
        busRepository.save(dtoToEntity(busDto,member));
    }

    public void deleteBusRoute(String name, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Long memberId = (Long)session.getAttribute("memberId");
        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow();
        BusEntity bus= busRepository.findByName(name).orElseThrow();
        busRepository.delete(bus);
    }

    public void updateBusRoute(BusDto busDto, HttpServletRequest request) {
        HttpSession session = request.getSession();

        Long memberId = (Long)session.getAttribute("memberId");
        MemberEntity member = memberRepository.findByMemberId(memberId).orElseThrow();
        BusEntity bus = busRepository.findByName(busDto.getName()).orElseThrow();
        bus.setUrl(busDto.getUrl());
    }

    private BusEntity dtoToEntity(BusDto dto, MemberEntity member) {
        return new BusEntity(dto.getName(),dto.getUrl(),member);
    }

    private BusDto EntityToDto(BusEntity busEntity) {
        return new BusDto(busEntity.getName(),busEntity.getUrl());
    }
}
