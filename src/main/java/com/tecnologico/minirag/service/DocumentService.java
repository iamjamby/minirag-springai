package com.tecnologico.minirag.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    private final VectorStore vectorStore;

    public DocumentService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void cargarDocumentos() throws IOException {
        System.out.println("------------------------------------");
        System.out.println("CARGANDO DOCUMENTOS PARA RAG");
        System.out.println("------------------------------------");

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:documentos/*.txt");

        List<Document> documentos = new ArrayList<>();

        if (resources != null) {
            for (Resource resource : resources) {
                System.out.println("Leyendo: " + resource.getFilename());
                TextReader reader = new TextReader(resource);
                List<Document> documentosArchivo = reader.get();
                documentos.addAll(documentosArchivo);
            }
        }

        System.out.println("Documentos encontrados: " + documentos.size());

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(300)
                .build();

        List<Document> chunks = splitter.apply(documentos);

        System.out.println("Chunks generados: " + chunks.size());

        if (!chunks.isEmpty()) {
            vectorStore.add(chunks);
        }

        System.out.println("Embeddings almacenados correctamente.");
        System.out.println("------------------------------------");
    }
}
