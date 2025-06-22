    public void editStudent() {
        // 假设这里有一个 ArrayList 叫做 students
        List<Student> students = getStudents(); // 获取学生的列表

        if (students.size() > 2) { // 检查大小是否大于2
            Student studentToEdit = students.get(2);
            // 继续处理...
        } else {
            System.out.println("Cannot edit student: Not enough students in the list.");
            // 或者抛出自定义异常、显示用户提示等
        }
    }

    private void editStudent(int id) {
        int index = queryStudent(id);
        if (index != -1){
            Student student = students.get(index);
            new EditStudentUI(this,student);
        }else {
            JOptionPane.showMessageDialog(this, "该学生不存在！");
        }
    }
}