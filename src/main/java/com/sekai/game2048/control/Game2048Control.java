package com.sekai.game2048.control;

import com.sekai.game2048.model.GameRecord;
import com.sekai.game2048.model.GameStats;
import com.sekai.game2048.model.Result;
import com.sekai.game2048.model.SaveGameRequest;
import com.sekai.game2048.model.User;
import com.sekai.game2048.service.Game2048Service;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Game2048Control {

    @Resource
    private Game2048Service game2048Service;

    @GetMapping("/game2048/stats")
    public Result<GameStats> stats(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("Please login first");
        }
        return Result.ok(game2048Service.getStats(user.getId()), "Loaded stats");
    }

    @GetMapping("/game2048/best-record")
    public Result<GameRecord> bestRecord(HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("Please login first");
        }
        return Result.ok(game2048Service.getBestRecord(user.getId()), "Loaded best record");
    }

    @GetMapping("/game2048/records")
    public Result<List<GameRecord>> records(@RequestParam(value = "limit", defaultValue = "20") int limit,
                                            HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("Please login first");
        }
        return Result.ok(game2048Service.listMyRecords(user.getId(), limit), "Loaded records");
    }

    @PostMapping("/game2048/record")
    public Result<GameRecord> saveRecord(@RequestBody SaveGameRequest request, HttpSession session) {
        Object value = session.getAttribute("loginUser");
        if (!(value instanceof User user)) {
            return Result.fail("Please login first");
        }
        return game2048Service.saveRecord(user.getId(), user.getNickName(), request);
    }
}
