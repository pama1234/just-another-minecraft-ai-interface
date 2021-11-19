var Opcodes=Java.type('org.objectweb.asm.Opcodes');
var InsnNode=Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
function initializeCoreMod() {
	return {
		'inputTransformer': {
			'target': {
				'type': 'METHOD',
				'class': 'net/minecraft/client/util/InputMappings',
				'methodName': 'grabOrReleaseMouse',
				'methodDesc': '(JIDD)V'
			},
			'transformer': function(method) {
				method.instructions.clear();
				method.instructions.add(new VarInsnNode(Opcodes.LLOAD,0));
				method.instructions.add(new VarInsnNode(Opcodes.ILOAD,2));
				method.instructions.add(new VarInsnNode(Opcodes.DLOAD,3));
				method.instructions.add(new VarInsnNode(Opcodes.DLOAD,5));
				//---
				method.instructions.add(new MethodInsnNode(
					Opcodes.INVOKESTATIC,
					"pama1234/mc/al/mod/MainMod","grabOrReleaseMouse","(JIDD)V"));
				//---
				method.instructions.add(new InsnNode(Opcodes.RETURN));
				return method;
			}
		}
	};
}
