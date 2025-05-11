package com.example.estoque.controller;

import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public List<ProdutoEntity> listarTodos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public ProdutoEntity buscarPorId(@PathVariable Long id) {
        return produtoService.buscarProdutoPorId(id);
    }

    @PostMapping
    public ProdutoEntity criar(@RequestBody ProdutoEntity produto) {
        return produtoService.criarProduto(produto);
    }
}
