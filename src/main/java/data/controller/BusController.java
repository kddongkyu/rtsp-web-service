package data.controller;

import data.object.dto.BusDto;
import data.object.dto.BusResponseDto;
import data.service.BusService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bus")
@CrossOrigin
public class BusController {

    private final BusService busService;

    public BusController(BusService busService) {
        this.busService = busService;
    }

    @PostMapping("/data")
    private ResponseEntity<BusResponseDto> getBusList(HttpServletRequest request) {
        return new ResponseEntity<>(busService.getAllBuses(request),HttpStatus.OK);
    }

    @PostMapping
    private ResponseEntity<String> addBusRoute(@RequestBody BusDto busDto, HttpServletRequest request) {
        busService.addBusRoute(busDto,request);
        return new ResponseEntity<>(busDto.getName() + " 노선 버스의 등록이 완료되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping
    private ResponseEntity<String> deleteBusRoute(@RequestParam("name") String name, HttpServletRequest request) {
        busService.deleteBusRoute(name,request);
        return new ResponseEntity<>(name+ " 노선 버스의 삭제가 완료되었습니다.", HttpStatus.OK);
    }

    @PutMapping
    private ResponseEntity<String> updateBusRoute(@RequestBody BusDto busDto, HttpServletRequest request) {
        busService.updateBusRoute(busDto,request);
        return new ResponseEntity<>(busDto.getName() + " 노선 버스의 수정이 완료되었습니다.", HttpStatus.OK);
    }
}
