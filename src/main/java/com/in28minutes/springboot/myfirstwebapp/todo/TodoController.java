package com.in28minutes.springboot.myfirstwebapp.todo;

import jakarta.validation.Valid;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoController {
    private TodoService todoService;

    public TodoController(TodoService todoService) {
        super();
        this.todoService = todoService;
    }

    @RequestMapping("list-todos")
    public String listAllTodos(ModelMap model) {
        List<Todo> todos = todoService.findByUserName("in28minutes");
        model.addAttribute("todos", todos);
        return "listTodos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodoPage(ModelMap modelMap) {
        String name = modelMap.get("name").toString();
        Todo todo = new Todo(0, name, "", LocalDate.now(), false);
        modelMap.put("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(ModelMap modelMap, @Valid Todo todo, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "todo";
        }
        String name = modelMap.get("name").toString();
        todoService.addTodo(name, todo.getDescription(), todo.getTargetDate(), false);
        return "redirect:list-todos";
    }

    @RequestMapping(value = "delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam int id) {
        todoService.deleteById(id);
        return "redirect:list-todos";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam int id, ModelMap modelMap) {
        Todo todo = todoService.findById(id);
        modelMap.put("todo", todo);
        return "todo";
    }

    @RequestMapping(value = "update-todo", method = RequestMethod.POST)
    public String updateTodoPage(ModelMap modelMap, @Valid Todo todo, BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "todo";
        }

        todo.setName("test");
        todo.setTargetDate(LocalDate.now().plusYears(3));
        todoService.updateTodo(todo);

        return "redirect:list-todos";
    }
}
