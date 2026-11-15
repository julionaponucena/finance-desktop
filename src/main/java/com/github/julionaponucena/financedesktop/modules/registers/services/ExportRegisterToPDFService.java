package com.github.julionaponucena.financedesktop.modules.registers.services;

import com.github.julionaponucena.financedesktop.modules.blockregister.repositories.BlockRegisterRepository;
import com.github.julionaponucena.financedesktop.modules.registers.data.inputs.ExportRegisterInputWrapper;
import com.github.julionaponucena.financedesktop.modules.registers.repositories.RegisterOutputGenerator;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ExportRegisterToPDFService {

    private final RegisterOutputGenerator outputGenerator;
    private final BlockRegisterRepository blockRegisterRepository;

    public void execute(ExportRegisterInputWrapper data) throws  Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassForTemplateLoading(ExportRegisterToPDFService.class, "/"); // templates na raiz de resources
        cfg.setDefaultEncoding("UTF-8");

        // 2. Carregar o template
        Template template = cfg.getTemplate("pdf-template.html");

        // 3. Preencher o template com a lista de objetos
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<Map<String, String>> itens = data.inputs().stream()
                .map(input -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("name", input.name());
                    item.put("date", input.date() != null ? input.date().format(dateFormatter) : "");
                    item.put("value", input.value());

                    return item;
                })
                .toList();

        final String title = this.blockRegisterRepository.getTitle(data.blockId());

        Map<String, Object> modelo = new HashMap<>();
        modelo.put("itens", itens); // A chave "itens" deve coincidir com o <#list itens>
        modelo.put("totalValue",data.totalValue());
        modelo.put("title", title);

        final String htmlPreenchido;
        try (StringWriter writer = new StringWriter()) {
            template.process(modelo, writer);
            htmlPreenchido = writer.toString();
        }

        // 4. Gerar o PDF com Flying Saucer
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlPreenchido);
        renderer.layout();

        try (OutputStream os = outputGenerator.generate()) {
            renderer.createPDF(os);
        }
    }
}
