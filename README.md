# Centro de Atención al Estudiante (CAE)

## Descripción general
El **Centro de Atención al Estudiante (CAE)** es un sistema desarrollado parala gestión de atención de estudiantes en una institución académica. Su objetivo principal es organizar las solicitudes o tickets de atención mediante el uso de estructuras de datos personalizadas, como **colas, pilas y listas enlazadas simples**, garantizando un control eficiente del flujo de atención y registro de actividades.

El sistema fue aplicando conceptos de **estructuras dinámicas** las cuales se estan estudiante, **programación orientada a objetos (POO)** y **modularidad del código**.

---

## Estructura del sistema
El sistema está organizado en tres paquetes principales:

- **`dominio`**: contiene las clases que representan los elementos principales del sistema, como `Ticket`, `Accion`, `Nota` y `EstadoTicket`.  
- **`estructuras`**: incluye las implementaciones propias de las estructuras de datos utilizadas (`Cola`, `Pila`, `ListaEnlazadaSimple` y `Nodo`).  
- **`controller`**: gestiona la lógica del programa mediante la clase `Caecontroller`, que coordina las operaciones de registro, atención y cierre de tickets.  
- **`Main.java`**: punto de entrada del programa, encargado de mostrar el menú principal y conectar las funciones del controlador con el usuario.

---

## Funcionamiento general
El sistema funciona a través de un menú interactivo en consola, desde el cual el usuario puede realizar las siguientes operaciones principales:

1. **Registrar un ticket de atención**: el estudiante ingresa una solicitud con su nombre y motivo del tramite.  
2. **Atender ticket**: el sistema utiliza una cola para atender a los estudiantes en el orden en que se registraron y sacaron un ticket.  
3. **Agregar notas o acciones**: cada ticket puede tener notas o acciones asociadas que se almacenan mediante listas o pilas para poder deshacer y rehacer notas.  
4. **Visualizar tickets pendientes y atendidos**: permite consultar el estado de la atención y los detalles de cada solicitud.  
5. **Salir del sistema**: finaliza la ejecución del programa.

Cada ticket pasa por distintos **estados** (pendiente, en atención, finalizado), definidos en la clase `EstadoTicket` la cual es Enum con opciones preestablecidas.  
El sistema mantiene la trazabilidad de las acciones realizadas para cada estudiante.

---

## Decisión del estilo y diseño
El estilo de programación utilizado sigue el paradigma **orientado a objetos**, con un diseño modular que separa claramente la **lógica del dominio** y las **estructuras de datos**.  
Se optó por un enfoque **didáctico**, utilizando nombres de clases y métodos claros, de modo que el código sea fácil de comprender y mantener.

Las estructuras (`Cola`, `Pila`, `ListaEnlazadaSimple`) fueron implementadas desde cero para reforzar los fundamentos de estructuras dinámicas y tener un mayor dominio de estas clases, y  así evitar la dependencia de colecciones nativas de Java.
La interfaz de usuario es sencilla y basada en consola, priorizando la comprensión de la lógica sobre la presentación gráfica.

---

## Guía de funcionamiento
1. Se encontrara un menu con las opciones del sistema.
   <img width="767" height="189" alt="image" src="https://github.com/user-attachments/assets/2c53a15c-e74a-4798-9854-8918f4894a1b" />
3. **La opción 1**, se podra realizar la creación de un ticket ingresando los parametros `Nombre` y `Tramite`.
   <img width="749" height="97" alt="image" src="https://github.com/user-attachments/assets/01b42cca-2f18-495d-802f-b9a9f9a2107c" />
5. **La opción 2**, puede atender el siguiente ticket en espera.
   <img width="832" height="55" alt="image" src="https://github.com/user-attachments/assets/93aa68bb-3a6b-428a-9e4a-5eaec0cc3f91" />
7. **Opción 3**, Si no hay un ticket en atención, lo va a asignar y despliega un nuevo menu donde podra gestionar las notas del tramite.
      En este nuevo menu, tiene las opciones de agregar notas agregando el parametro `Notas`, puede deshacer notas, rehacerlas, y finalizar el tramite imprimiendo el historial.
      <img width="827" height="320" alt="image" src="https://github.com/user-attachments/assets/9a28e30e-4bae-4698-8d0c-0cedd9b53bd2" />
9. **Opción 4**, muestra los tickets que estan en la cola esperando su turno.
    <img width="1064" height="470" alt="image" src="https://github.com/user-attachments/assets/f0fbdab3-a993-414d-9f2b-a2243ca9151a" />
11. **Opción 5**, muestra los tickets que ya fueron atendidos, imprimiendo con su historial de acciones.
    <img width="1070" height="554" alt="image" src="https://github.com/user-attachments/assets/a8784d81-84d9-4e23-a65e-8a83b47c8247" />
13. **Opción 6**, Finaliza el programa.
    <img width="1040" height="355" alt="image" src="https://github.com/user-attachments/assets/09edb3be-d5a6-44f4-910b-62b38ae220c7" />
---

## Fuentes de apoyo
Durante el desarrollo de las clases genéricas y las estructuras de datos, se realizaron consultas a diversas fuentes técnicas para comprender las mejores prácticas en la implementación de estructuras dinámicas en Java:

- Se realizo consultas en IA´s generativas como ChatGPT y Gemini para mantener una asistencia conceptual y validación de sintaxis de el uso de clases genericas tanto en el nodo como en el resto de estructuras dinamicas. Ademas de como poderlas implementar en el dominio.    
- Tambien se realizo consultas a fuentes de foros en los cuales entendiamos como utilizar las estructuras dinamicas tanto usando librerias de java como la opción que utilizamos nosotros como la de no ocupar librerias o api's de java. [Software Engineering Stack Exchange – Diseño de una pila sin usar bibliotecas de Java](https://softwareengineering.stackexchange.com/questions/235318/how-to-design-a-stack-without-using-java-library-or-using-list)  [Universidad Carlos III de Madrid – Guía sobre pilas y colas en Java](https://www.it.uc3m.es/java/git-gisc-2013-14/units/pilas-colas/guides/2/guide_es_solution.html)

---

## Créditos
**Autores:** 
  - Fernando Patiño (Fede-11)
  - Francis Valdiviezo (FrancisValdiviezoUNL)
  - Jostin Vasquez (xDylok)
  - Gyna Yupanqui (Gyna-22)
  - Ismael Gonzalez (IsmaelGonz)

**Versión:** 1.1  
**Fecha:** 21 - 10 - 2025  
**Proyecto académico:** Centro de Atención al Estudiante (CAE)
