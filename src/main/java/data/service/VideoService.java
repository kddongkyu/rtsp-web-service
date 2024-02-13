package data.service;

import data.object.entity.MemberEntity;
import data.repository.BusRepository;
import data.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class VideoService {

    private final MemberRepository memberRepository;
    private final BusRepository busRepository;
    private int streamUrlLastIndex;

    public VideoService(MemberRepository memberRepository, BusRepository busRepository) {
        this.memberRepository = memberRepository;
        this.busRepository = busRepository;
    }

    public boolean newSession(String busRoute, HttpServletRequest request) {
        log.info("Starting new session for bus route: " + busRoute);
        String url = busRepository.findByName(busRoute).orElseThrow().getUrl();

        int portNumber = 0;
        HttpSession session = request.getSession();

        if (session.getAttribute("login").toString().equals("true")) {
            MemberEntity targetMember = memberRepository.findByMemberId((Long) session.getAttribute("memberId")).orElseThrow();
            portNumber = targetMember.getIndividualPort();
        }

        boolean updateResult = updateFile(url, String.valueOf(portNumber));
        log.info("Session update for " + busRoute + " is " + (updateResult ? "successful" : "failed"));
        return updateResult;
    }

    private boolean updateFile(String url, String socketNumber) {
        try {
            log.info("Updating file for socket number: " + socketNumber);

            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "/home/kim/update_script.sh", socketNumber);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            int exitVal = process.waitFor();
            if (exitVal != 0) {
                log.error("Shell script execution failed with exit code " + exitVal);
                return false;
            }

            Path path = Paths.get("/home/kim/node-file/" + socketNumber + "/index.js");
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

            int last = findIndex(content, "streamUrl");
            String targetString = content.substring(streamUrlLastIndex, last);
            content = content.replace(targetString, url);
            Files.writeString(path, content);

            int last2 = findIndex(content, "wsPort");
            String targetString2 = content.substring(streamUrlLastIndex - 1, last2);
            content = content.replace(targetString2, socketNumber);
            Files.writeString(path, content);

            Process killProcess = Runtime.getRuntime().exec("kill -9 node /home/kim/node-file/" + socketNumber + "/index.js");
            killProcess.waitFor();

            Runtime.getRuntime().exec("nohup node /home/kim/node-file/" + socketNumber + "/index.js");

            log.info("Node file updated and process restarted for socket number: " + socketNumber);
            return true;
        } catch (IOException e) {
            log.error("IOException occurred while updating file for socket number: " + socketNumber, e);
            return false;
        } catch (InterruptedException e) {
            log.error("InterruptedException occurred while updating file for socket number: " + socketNumber, e);
            throw new RuntimeException(e);
        }
    }

    private int findIndex(String content, String word) {
        int streamUrlFirstIndex = content.indexOf(word);

        streamUrlLastIndex = streamUrlFirstIndex + word.length() + 3;

        int lastIndexOfUrl = 0;

        int index = streamUrlLastIndex;
        if (word.equals("wsPort")) {
            while (true) {
                if (content.charAt(index) == ',') {
                    lastIndexOfUrl = index;
                    break;
                } else {
                    index++;
                }
            }
        } else {
            while (true) {
                if (content.charAt(index) == '"') {
                    lastIndexOfUrl = index;
                    break;
                } else {
                    index++;
                }
            }
        }
        return lastIndexOfUrl;
    }

    private boolean executeCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitVal = process.waitFor(); // 이 부분이 실행될 때까지 대기
            if (exitVal == 0) {
                return true; // 정상 종료
            } else {
                log.error("Command execution failed with exit code " + exitVal);
                return false; // 비정상 종료
            }
        } catch (IOException | InterruptedException e) {
            log.error("Command execution error", e);
            return false;
        }
    }
}
