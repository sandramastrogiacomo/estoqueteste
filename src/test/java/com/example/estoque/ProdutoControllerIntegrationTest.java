package com.example.estoque;

import com.example.estoque.controller.ProdutoController;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testListarTodos() throws Exception {
        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setId(1L);
        produto1.setNome("Caneta");
        produto1.setDescricao("Azul");
        produto1.setPreco(1.50);
        produto1.setQtd(100);

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setId(2L);
        produto2.setNome("Lápis");
        produto2.setDescricao("Preto");
        produto2.setPreco(0.80);
        produto2.setQtd(200);

        List<ProdutoEntity> produtos = Arrays.asList(produto1, produto2);

        Mockito.when(produtoService.listarProdutos()).thenReturn(produtos);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Caneta"))
                .andExpect(jsonPath("$[1].nome").value("Lápis"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Caderno");
        produto.setDescricao("Grande");
        produto.setPreco(20.00);
        produto.setQtd(50);

        Mockito.when(produtoService.buscarProdutoPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Caderno"))
                .andExpect(jsonPath("$.descricao").value("Grande"))
                .andExpect(jsonPath("$.preco").value(20.00))
                .andExpect(jsonPath("$.qtd").value(50));
    }

    @Test
    public void testCriarProduto() throws Exception {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Caneta");
        produto.setDescricao("Azul");
        produto.setPreco(1.50);
        produto.setQtd(100);

        Mockito.when(produtoService.criarProduto(any(ProdutoEntity.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Caneta"))
                .andExpect(jsonPath("$.descricao").value("Azul"))
                .andExpect(jsonPath("$.preco").value(1.50))
                .andExpect(jsonPath("$.qtd").value(100));
    }

    @Test
    public void testAtualizarProduto() throws Exception {
        ProdutoEntity produtoAtualizado = new ProdutoEntity();
        produtoAtualizado.setId(1L);
        produtoAtualizado.setNome("Caneta Vermelha");
        produtoAtualizado.setQtd(150);

        Mockito.when(produtoService.atualizarProduto(any(ProdutoEntity.class))).thenReturn(produtoAtualizado);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Caneta Vermelha"))
                .andExpect(jsonPath("$.qtd").value(150));
    }

    @Test
    public void testDeletarProduto() throws Exception {
        Mockito.doNothing().when(produtoService).deletarProduto(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isOk());
    }
}
