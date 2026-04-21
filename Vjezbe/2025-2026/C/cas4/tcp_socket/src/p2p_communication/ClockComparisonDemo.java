package p2p_communication;

import java.util.Arrays;

public class ClockComparisonDemo {

	public static void main(String[] args) {
		System.out.println("=== Demonstracija: Lamport vs Vektorski sat ===\n");

		System.out.println("--- LAMPORT CLOCK ---\n");

		LamportClock lc0 = new LamportClock();
		LamportClock lc1 = new LamportClock();
		LamportClock lc2 = new LamportClock();

		int lc0_local = lc0.tick();
		System.out.println("Node 0: lokalni dogadjaj          -> LC=" + lc0_local);

		int lc0_send = lc0.sendTick();
		System.out.println("Node 0: SEND -> Node 1             -> LC=" + lc0_send);

		int lc1_recv = lc1.receiveUpdate(lc0_send);
		System.out.println("Node 1: RECV <- Node 0             -> LC=" + lc1_recv);

		int lc1_local = lc1.tick();
		System.out.println("Node 1: lokalni dogadjaj (e_B)     -> LC=" + lc1_local);

		int lc2_val = 0;
		for (int i = 0; i < 4; i++) {
			lc2_val = lc2.tick();
		}
		System.out.println("Node 2: 4 lokalna dogadjaja (e_C)  -> LC=" + lc2_val);

		System.out.println();
		System.out.println("Poredjenje sa Lamport satom:");
		System.out.println("  e_B (Node 1): LC=" + lc1_local);
		System.out.println("  e_C (Node 2): LC=" + lc2_val);
		System.out.println("  LC(e_B) == LC(e_C) => " + (lc1_local == lc2_val));
		System.out.println("  PROBLEM: Ne mozemo zakljuciti da li su e_B i e_C konkurentni");
		System.out.println("           ili je jedan uzrocno prije drugog!");
		System.out.println();
		System.out.println("  Takodje: Node 0 send (LC=" + lc0_send + ") < e_C (LC=" + lc2_val + ")");
		System.out.println("  Ali oni NISU u uzrocnoj vezi - Lamport daje lazni utisak reda!");

		System.out.println("\n--- VEKTORSKI CLOCK ---\n");

		VectorClock vc0 = new VectorClock(0, 3);
		VectorClock vc1 = new VectorClock(1, 3);
		VectorClock vc2 = new VectorClock(2, 3);

		int[] vc0_local = vc0.tick();
		System.out.println("Node 0: lokalni dogadjaj          -> VC=" + Arrays.toString(vc0_local));

		int[] vc0_send = vc0.sendTick();
		System.out.println("Node 0: SEND -> Node 1             -> VC=" + Arrays.toString(vc0_send));

		int[] vc1_recv = vc1.receiveUpdate(vc0_send);
		System.out.println("Node 1: RECV <- Node 0             -> VC=" + Arrays.toString(vc1_recv));

		int[] vc1_local = vc1.tick();
		System.out.println("Node 1: lokalni dogadjaj (e_B)     -> VC=" + Arrays.toString(vc1_local));

		int[] vc2_val = null;
		for (int i = 0; i < 4; i++) {
			vc2_val = vc2.tick();
		}
		System.out.println("Node 2: 4 lokalna dogadjaja (e_C)  -> VC=" + Arrays.toString(vc2_val));

		System.out.println();
		System.out.println("Poredjenje sa vektorskim satom:");
		System.out.println("  e_B (Node 1): VC=" + Arrays.toString(vc1_local));
		System.out.println("  e_C (Node 2): VC=" + Arrays.toString(vc2_val));

		String relation = compareVectors(vc1_local, vc2_val);
		System.out.println("  Relacija: " + relation);

		System.out.println();
		System.out.println("  Node 0 send: VC=" + Arrays.toString(vc0_send) + "  vs  e_C: VC=" + Arrays.toString(vc2_val));
		String relation2 = compareVectors(vc0_send, vc2_val);
		System.out.println("  Relacija: " + relation2);
		System.out.println("  Vektorski sat ISPRAVNO detektuje da su konkurentni!");
	}

	
	static String compareVectors(int[] a, int[] b) {
		boolean aLeqB = true; // a[i] <= b[i] za sve i
		boolean bLeqA = true; // b[i] <= a[i] za sve i

		for (int i = 0; i < a.length; i++) {
			if (a[i] > b[i]) aLeqB = false;
			if (b[i] > a[i]) bLeqA = false;
		}

		if (aLeqB && bLeqA) return "a == b (isti dogadjaj)";
		if (aLeqB)          return "a -> b (a happened-before b)";
		if (bLeqA)          return "b -> a (b happened-before a)";
		return "a || b (KONKURENTNI - nema uzrocne veze!)";
	}
}
