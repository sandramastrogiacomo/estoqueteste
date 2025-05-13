package com.example.estoque;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import com.example.estoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarProduto() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setNome("Caneta");
        produto.setQtd(10);

        when(produtoRepository.save(produto)).thenReturn(produto);

        ProdutoEntity resultado = produtoService.criarProduto(produto);

        assertEquals("Caneta", resultado.getNome());
        assertEquals(10, resultado.getQtd());
        verify(produtoRepository).save(produto);
    }

    @Test
    public void testListarProdutos() {
        ProdutoEntity produto1 = new ProdutoEntity();
        produto1.setNome("Caneta");

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setNome("Lápis");

        List<ProdutoEntity> listaSimulada = Arrays.asList(produto1, produto2);

        when(produtoRepository.findAll()).thenReturn(listaSimulada);

        List<ProdutoEntity> resultado = produtoService.listarProdutos();

        assertEquals(2, resultado.size());
        assertEquals("Caneta", resultado.get(0).getNome());
        assertEquals("Lápis", resultado.get(1).getNome());
    }

    @Test
    public void testBuscarProdutoPorId() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Caderno");

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoEntity resultado = produtoService.buscarProdutoPorId(1L);

        assertEquals("Caderno", resultado.getNome());
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testAtualizarEstoqueComSucesso() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Borracha");
        produto.setQtd(10);

        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setQtd(5);

        Pedido pedido = new Pedido();
        pedido.setItens(List.of(item));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produto);

        assertDoesNotThrow(() -> produtoService.atualizarEstoque(pedido));

        assertEquals(5, produto.getQtd());
        verify(produtoRepository).save(produto);
    }

    @Test
    public void testAtualizarEstoqueComQuantidadeInsuficiente() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Tesoura");
        produto.setQtd(3);

        ItemPedido item = new ItemPedido();
        item.setId(1L);
        item.setQtd(5);

        Pedido pedido = new Pedido();
        pedido.setItens(List.of(item));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        ForaDeEstoqueException exception = assertThrows(
                ForaDeEstoqueException.class,
                () -> produtoService.atualizarEstoque(pedido)
        );

        assertTrue(exception.getMessage().contains("Produto Tesoura possui apenas: 3 em estoque"));
        verify(produtoRepository, never()).save(any());
    }

    @Test
    public void testAtualizarProdutoComSucesso() {
        ProdutoEntity existente = new ProdutoEntity();
        existente.setId(1L);
        existente.setNome("Produto Antigo");
        existente.setDescricao("Desc antiga");
        existente.setPreco(10.0);
        existente.setQtd(5);

        ProdutoEntity atualizado = new ProdutoEntity();
        atualizado.setId(1L);
        atualizado.setNome("Produto Novo");
        atualizado.setDescricao("Nova descrição");
        atualizado.setPreco(15.0);
        atualizado.setQtd(8);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(atualizado);

        ProdutoEntity resultado = produtoService.atualizarProduto(atualizado);

        assertEquals("Produto Novo", resultado.getNome());
        assertEquals("Nova descrição", resultado.getDescricao());
        assertEquals(15.0, resultado.getPreco());
        assertEquals(8, resultado.getQtd());
    }

    @Test
    public void testAtualizarProdutoNaoEncontrado() {
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(99L);

        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            produtoService.atualizarProduto(produto);
        });

        assertTrue(ex.getMessage().contains("Produto não encontrado com ID: 99"));
    }

    @Test
    public void testDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> produtoService.deletarProduto(1L));

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    public void testDeletarProdutoNaoEncontrado() {
        when(produtoRepository.existsById(2L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            produtoService.deletarProduto(2L);
        });

        assertTrue(ex.getMessage().contains("Produto com ID 2 não encontrado."));
        verify(produtoRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testEncontrarPorNome() {
        ProdutoEntity entity = new ProdutoEntity();
        entity.setNome("Café");
        entity.setDescricao("Café torrado e moído");
        entity.setPreco(18.99);
        entity.setQtd(20);

        when(produtoRepository.findByNome("Café")).thenReturn(entity);

        com.example.estoque.domain.Produto resultado = produtoService.encontrarPorNome("Café");

        assertEquals("Café", resultado.getNome());
        assertEquals("Café torrado e moído", resultado.getDescricao());
        assertEquals(18.99, resultado.getPreco());
        assertEquals(20, resultado.getQtd());
    }

}
