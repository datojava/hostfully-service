package com.hostfully.webservice.controllers;

import com.hostfully.webservice.annotations.Monitor;
import com.hostfully.webservice.models.HostfullyResponse;
import com.hostfully.webservice.models.blocks.BlockInfo;
import com.hostfully.webservice.services.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BlockController {

    private final BlockService blockService;

    @Autowired
    public BlockController(final BlockService blockService) {
        this.blockService = blockService;
    }



    @Monitor
    @PostMapping("block/create")
    public HostfullyResponse create(@RequestBody BlockInfo blockInfo) throws Exception {

        return blockService.createBlock(blockInfo);

    }

    @Monitor
    @PutMapping("block/update")
    public HostfullyResponse update(@RequestBody BlockInfo blockInfo) throws Exception {

        return blockService.updateBlock(blockInfo);

    }

    @Monitor
    @DeleteMapping("block/delete/{id}")
    public HostfullyResponse delete(@PathVariable Long id) throws Exception {

        return blockService.deleteBlock(id);

    }
}
