package com.InventoryManagmentUniandes.InventoryManagmentUniandes;

import com.InventoryManagmentUniandes.InventoryManagmentUniandes.service.InventoryManagementService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Application {
	
	@Autowired
	private InventoryManagementService inventoryService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * Carga automáticamente los datos de ejemplo cuando la aplicación inicia
	 * Si falla, solo muestra una advertencia pero el servidor sigue funcionando
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void initializeData() {
		try {
			System.out.println("🔄 Intentando cargar usuarios en Firebase...");
			inventoryService.seedExampleData();
			System.out.println("✅ Usuarios cargados: adan/123, juan/456, maria/789");
		} catch (IllegalStateException e) {
			System.out.println("⚠️ IMPORTANTE: Debes habilitar Firestore API en tu proyecto Firebase");
			System.out.println("   Ve a: https://console.developers.google.com/apis/api/firestore.googleapis.com");
			System.out.println("   Selecciona tu proyecto: inventorymanagment-131df");
			System.out.println("   Haz clic en ENABLE (Habilitar)");
			System.out.println("   Luego reinicia este servidor");
		} catch (Exception e) {
			System.out.println("⚠️ No se pudieron cargar usuarios automáticamente: " + e.getMessage());
			System.out.println("   El servidor sigue funcionando, pero algunos datos no estará disponibles");
		}
	}

}
