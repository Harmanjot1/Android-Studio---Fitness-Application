package com.example.app1.Exercise;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.app1.Exercise.Exercise_Adapter;
import com.example.app1.Exercise.Exercise_Item;
import com.example.app1.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;


public class Exercises_frag extends Fragment {

    private final ArrayList<Exercise_Item> exercise_items = new ArrayList<>();
    private final ArrayList<Exercise_Item> bicep = new ArrayList<>();
    private final ArrayList<Exercise_Item> tricep = new ArrayList<>();
    private final ArrayList<Exercise_Item> chest = new ArrayList<>();
    private final ArrayList<Exercise_Item> shoulders = new ArrayList<>();
    private final ArrayList<Exercise_Item> back = new ArrayList<>();
    private final ArrayList<Exercise_Item> forearm = new ArrayList<>();
    private final ArrayList<Exercise_Item> core = new ArrayList<>();
    private final ArrayList<Exercise_Item> glutes = new ArrayList<>();
    private final ArrayList<Exercise_Item> upper_legs = new ArrayList<>();
    private final ArrayList<Exercise_Item> cardio = new ArrayList<>();


    private RecyclerView mRecyclerView;
    private Exercise_Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_exercises_frag, container, false);

        //showing action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        // initialising recyclerview id
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView2);
        setHasOptionsMenu(true);
        BottomNavigationView bottomNavigationView = v.findViewById(R.id.bottomNavigationView_exercise);
        NavController navController = Navigation.findNavController(getActivity(), R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        getActivity().setTitle("Exercises");

        //---------------------------------------------------------------------------------------------------------------
        //Exercises
        //Chest
        chest.add(new Exercise_Item(R.drawable.benchpress1,R.drawable.benchpress2, "Bench Press", "Lie face up on a flat bench, and grip a barbell with the hands slightly wider than shoulder-width.\n\nPress the feet into the ground and the hips into the bench while lifting the bar off the rack. Slowly lower the bar to the chest by allowing the elbows to bend out to the side. Stop when the elbows are just below the bench, and press feet into the floor to press the weight straight up to return to the starting position."));
        chest.add(new Exercise_Item(R.drawable.lying_pullovers1,R.drawable.lying_pullovers2, "Lying Pullovers", "Lie face up on a bench with the feet resting comfortably on the floor, and straighten the arms above the chest while holding one dumbbell in each hand with the palms facing each other.\n\nSlowly stretch the arms up overhead while keeping the elbows straight. Once the arms are overhead slowly pull the weights back up to the original starting position. Repeat for the desired number of repetitions."));
        chest.add(new Exercise_Item(R.drawable.seated_cable_press1,R.drawable.seated_cable_press2, "Seated Cable Press", "Starting Position: Sit with your back firmly supported against the backrest. If adjustable, align the handles level with your shoulder or mid-chest region.\n\nGrasp the handles and position your hands level with your mid-chest region, maintaining a neutral wrist position (i.e., wrists in line with your forearms).  Position your feet firmly on the floor to stabilize your body. \nStiffen (“brace”) your abdominal muscles to stabilize your spine, but do not press your low back into the backrest. Maintain the natural arch in your low back and avoid arching your back throughout the exercise. Depress and retract your scapulae (pull shoulders back and down) and attempt to hold this position throughout the exercise."));
        chest.add(new Exercise_Item(R.drawable.bent_over_row1,R.drawable.bent_over_row2, "Bent Knee Push-up", "Step 1: Kneel on an exercise mat or floor and bring your feet together behind you.\n"+"\n"+"Step 2: Slowly bend forward to place your palms flat on the mat, positioning your hands shoulder-width apart with your fingers facing forward. Slowly shift your weight forward until your shoulders are positioned directly over your hands.\n"+"\n"+"Step 3: Downward Phase: Slowly lower your body towards the floor while maintaining a rigid torso and head aligned with your spine. Do not allow your low back to sag or your hips to hike upwards during this downward phase.\n"+"\n"+"Step 4: Upward Phase: Press upwards through your arms while maintaining a rigid torso and head aligned with your spine"));
        //chest.add(new Exercise_Item(R.drawable.barbell_pullover,R.drawable.bench_cable_flys, "Barbell Press", ""));
        //Biceps
        bicep.add(new Exercise_Item(R.drawable.bicep_curl1,R.drawable.bicep_curl2, "Bicep Curl", "Hold the barbell with both hands facing up so the wrists, elbows, and shoulders are in a straight line about shoulder-width apart.\nLift the barbell toward the shoulders while bending the elbows and keeping them next to the middle of the body. Slowly lower the weight to return to the starting position. Keep chest still, using just the arms for the movement."));
        bicep.add(new Exercise_Item(R.drawable.rotational_uppercut1,R.drawable.rotational_uppercut2, "Rotational Uppercut", "Stand with the feet hip-width apart, hold one dumbbell in each hand with the palms facing up, and the elbows close to the rib cage. Sink back into the hips and press the left foot into the ground to turn to the right, keep the left elbow bent and perform an uppercut with the left arm, bringing the left elbow to shoulder-height.\nSlowly lower the left arm and turn back to the center. Push the right foot into the ground to turn the left hip while swinging the right arm up in an uppercut."));
        bicep.add(new Exercise_Item(R.drawable.standing_bicep_curl1,R.drawable.standing_bicep_curl2, "Standing Bicep Curl", "Place the center of a resistance band under one foot and grasp the handles with one in each hand.\nWith the palms facing the ceiling, bend the elbows to bring the hands up toward the shoulders. Keep the elbows in close to the sides through the movement, and lower slowly back down to complete the repetition."));
        bicep.add(new Exercise_Item(R.drawable.reverse_bicep_curl1,R.drawable.reverse_bicep_curl2, "Reverse Bicep Curl", "Hold the barbell about shoulder-width apart with the palms facing down and hands in a straight line with shoulders and elbows. Lift the bar towards the shoulders while bending at the elbows and keeping them down close to the sides of the body.\n\nSlowly lower the weight to return to the starting position."));

        // Triceps
        tricep.add(new Exercise_Item(R.drawable.lying_barbell_triceps_extensions1,R.drawable.lying_barbell_triceps_extensions2, "Lying Barbell Triceps Extensions", "Step 1: Starting Position: Holding a barbell with a closed, pronated grip (palms facing your feet and thumbs wrapped around the bar), lie supine (on your back) on a flat bench with your feet firmly placed on the floor. Slowly press the barbell off your chest, fully extending your elbows until the barbell is positioned directly above your face with your palms still facing towards your feet. Maintain a neutral wrist position (no flexion or extension at the wrist).\n"+"\n"+"Step 2: Downward Phase: Inhale and slowly bend the elbows, lowering the bar in a controlled manner towards your forehead or slightly behind your head while maintaining your neutral wrist position.\n"+"\n"+"Step 3: Upward Phase: Exhale and slowly return to your starting position with your elbows fully extended."));
        tricep.add(new Exercise_Item(R.drawable.benchpress1,R.drawable.benchpress2, "Chest Press", "Lie face up on a flat bench, and grip a barbell with the hands slightly wider than shoulder-width. Press the feet into the ground and the hips into the bench while lifting the bar off the rack.\n\nSlowly lower the bar to the chest by allowing the elbows to bend out to the side. Stop when the elbows are just below the bench, and press feet into the floor to press the weight straight up to return to the starting position."));
        tricep.add(new Exercise_Item(R.drawable.triceps_pushdowns1,R.drawable.triceps_pushdowns2, "Triceps Pushdowns", "Step 1: Starting Position: Stand facing the cable machine and position the cable attachment at a height above your head. \n"+"\n"+"Step 2: Gently exhale and slowly extend your elbows, pressing the handle / rope down towards the floor. Maintain your torso, shoulder, arm and wrist position throughout the movement \n\n"+"Step 3: Continue pressing the weight with your elbows right next to your side while maintaining a neutral position with your wrists until your elbows become fully extended, (do not allow them to \"lock out\" in full extension)."));
        tricep.add(new Exercise_Item(R.drawable.trx___suspended_push_up1,R.drawable.trx___suspended_push_up2, "TRX ® Suspended Push-up", "Step 1: Starting Position: Place your feet securely into the foot cradles positioned directly under the anchor point. Apply downward pressure with the tops of your feet by plantarflexing your ankles (toes point away from your shins).\n"+"\n"+"Step 2: Upward Phase: Exhale and slowly press yourself upward until your elbows are fully extended, aligning your head and spine (avoid any sagging or aching in your low back or hips hiking upwards). \n"+"\n"+"Step 3: Downward Phase: Maintaining your rigid torso, inhale and slowly lower your body towards the floor, touching your chin or upper chest to the floor.\n"+"\n"+"Step 4: Exercise Variation: The intensity of this exercise can be increased by lengthening the straps and positioning your body further away from under the anchor point of the TRX."));

        // Back
        back.add(new Exercise_Item(R.drawable.bent_over_row1,R.drawable.bent_over_row2, "Bent-over Row", "Grip a barbell with palms down so that the wrists, elbows, and shoulders are in a straight line. Lift the bar from the rack, bend forward at the hips, and keep the back straight with a slight bend in the knees. Lower the bar towards the floor until the elbows are completely straight, and keep the back flat as the bar is pulled towards the belly button. Then slowly lower the bar to the starting position and repeat. \n"+"\n"+"Step 2: Exhale and depress and retract your scapulae (pull shoulders down and back) without arching your low back. Hold the contraction for 5-10 seconds for a total of 2-4 repetitions."));
        back.add(new Exercise_Item(R.drawable.shoulder_packing1,R.drawable.shoulder_packing2, "Shoulder Packing", "Step 1: Starting Position: Stand with your feet hip-width apart, toes pointing forward, with arms by your sides.\n\nContract your abdominal muscles (“brace”) to stabilize your spine while holding your chest up and out with your head tilted slightly up. "));
        back.add(new Exercise_Item(R.drawable._0_lat_stretch1,R.drawable._0_lat_stretch2, "90 Lat Stretch", "Step 1: Starting Position:  Stand with your feet hip-width apart with arms by your sides. Stiffen your abdominal muscles (“brace”) to stabilize your spine, then depress and retract your scapulae (pull shoulders down and back) without arching your low back. Hold your chest up and out, tilt your head slightly up.\n"+"\n"+"Step 2: With a slight bend in the knee, shift your weight over your heels and slowly begin bending forward at the hips, maintaining the abdominal bracing and flat back.\n"+"\n"+"Step 3: With your hands on the table, keep your legs directly under your hips while leaning back into your hips, straightening the legs and drawing your torso closer to the ground while keeping a flat back.\n"+"\n"+"Step 4: Hold the stretch position for 15-30 seconds for a total of 2-4 repetitions."));
        back.add(new Exercise_Item(R.drawable.romanian_deadlift1,R.drawable.romanian_deadlift2, "Romanian Deadlift", "Holding a barbell with both hands so that it rests on the front of the thighs, keep a slight bend in both knees and a straight back.\n\nPush the hips back while lowering the weight towards the floor until feeling some tension along the back of the legs. To return to standing, push the heels into the floor and pull the knees backwards, keeping the bar very close to the body while standing."));
        //shoulders
        shoulders.add(new Exercise_Item(R.drawable.seated_shoulder_press1,R.drawable.seated_shoulder_press2, "Seated Shoulder Press", "Sit in a shoulder-press bench and rack, and keep the back straight while gripping the bar with the hands a little wider than shoulder-width apart. Take the bar out of the rack and slowly lower to the front of the shoulders, stopping when the bar is about chin-height.\n\nPress the feet into the floor, squeeze the stomach muscles, and keep the elbows pointed forward while pressing the bar directly overhead. Slowly lower the bar to chin-height and repeat for the desired number of repetitions. "));
        shoulders.add(new Exercise_Item(R.drawable.standing_shoulder_press1,R.drawable.standing_shoulder_press2, "Standing Shoulder Press", "Place a barbell in a rack at about shoulder-height. Grip the bar with the hands about shoulder-width apart and the palm facing the ceiling. \n\nDip under the bar to bring it off the rack letting it rest across the top of the shoulders so that the palms are facing the ceiling and the elbows are pointed straight ahead. Step back, keeping the back straight and tall and press the barbell directly overhead. Slowly return the weight to the shoulders and repeat for the desired number of reps."));
        shoulders.add(new Exercise_Item(R.drawable.diagonal_raise1,R.drawable.diagonal_raise2, "Diagonal Raise", "Stand with the feet hip-width apart, hold one dumbbell in the left hand with the left arm down straight and the left palm resting in front of the right thigh.\n\nPress the feet into the ground, keep the hips straight and the back tall. Raise the left arm across the body and out to the left side so that the weight is brought up to shoulder-height and the arm is kept straight."));
        shoulders.add(new Exercise_Item(R.drawable.incline_reverse_fly1,R.drawable.incline_reverse_fly2, "Incline Reverse Fly", "Step 1: Starting Position: Holding two dumbbells, sit on a bench facing the backrest angled at 45 to 60 degrees. Hold your torso against the backrest and keep your feet firmly placed on the floor. Allow your arms to hang towards the floor with your elbows slightly flexed and palms facing each other.\n"+"\n"+"Step 2: Upward Phase: Exhale and slowly raise the dumbbells up and out to the sides, raising the upper arms, elbows and dumbbells in unison until the arms are near level with the shoulders. Squeeze your scapulae (shoulder blades) together as you reach the end position. In the raised position, the dumbbells should be aligned with, or slightly in front of your ears.\n"+"\n"+"Step 3: Downward Phase: Gently inhale and lower the dumbbells in unison back to your starting position."));
        // core
        core.add(new Exercise_Item(R.drawable.crunch1,R.drawable.crunch2, "Crunch", "Step 1: Starting Position: Lie in a supine (on your back) position on a mat with your knees bent, feet flat on the floor and heels 12 - 18\" from your tailbone.\n"+"\n"+"Step 2: Place your hands behind your head, squeezing your scapulae (shoulder blades) together and pulling your elbows back without arching your low back. \n"+"\n"+"Step 3: Upward Phase: Exhale, contract your abdominal and core muscles and flex your chin slightly towards your chest while slowly curling your torso towards your thighs. The movement should focus on pulling your rib cage towards your pelvis (the neck stays relaxed while the chin is tucked towards the neck).\n"+"\n"+"Step 4: Downward Phase: Gently inhale and slowly uncurl (lower) your torso back towards the mat in a controlled fashion keeping your feet, tailbone and low back in contact with the mat."));
        core.add(new Exercise_Item(R.drawable.decline_plank1,R.drawable.decline_plank2, "Decline Plank", "With the feet close together on top of the BOSU dome, place the elbows on the ground underneath the shoulders with the hands in fists. \n\nSqueeze the muscles of the stomach and glute, and keep the body in a straight line from head to heels. Hold this position for the desired amount of time."));
        core.add(new Exercise_Item(R.drawable.pull_over_crunch1,R.drawable.pull_over_crunch2, "Pull-over Crunch", "Lie face up flat on the ground with the feet flat on the floor and the knees bent up in the air.\n\nPlace a kettlebell on the ground over the head, reach overhead with both hands, and grip the kettlebell by the handles. While keeping the arms straight, pull the kettlebell over the head to directly above the chest.\nHold the kettlebell over the chest and draw the belly button in towards the ground to help pull the rib cage towards the hips, lifting the back off the ground to perform a crunch. Lower slowly back to the floor, and once the back is flat on the ground lower the kettlebell back overhead."));
        core.add(new Exercise_Item(R.drawable.quadruped_bent_knee_hip_extensions1,R.drawable.quadruped_bent_knee_hip_extensions2, "Quadruped Bent-knee", "Step 1: Starting Position: Kneel on an exercise mat or floor, positioning your knees and feet hip-width apart, with your feet plantar-flexed (toes pointing away from your body). Slowly lean forward to place your hands on the mat, positioning them directly under your shoulders at shoulder-width with your fingers facing forward. \n"+"\n"+"Step 2: Maintain a strong, stable core and squeeze (contract) the left gluteus maximus (butt) to lift the left leg while maintaining your bent-knee position, allowing movement only from the left hip joint. \n"+"\n"));
        //glutes
        glutes.add(new Exercise_Item(R.drawable.elevated_glute_bridge1,R.drawable.elevated_glute_bridge2, "Elevated Glute Bridge", "Lie on a flat bench so that the body is perpendicular to the bench with the upper back and shoulders lying across the pad. Press the feet into the ground about hip-width apart, hold one dumbbell in each hand and rest them on top of the hips to add external resistance. \n\nSlowly lower the tailbone towards the floor while holding the weights on the hips, and to return to the top press both feet into the floor and squeeze the glute muscles."));
        glutes.add(new Exercise_Item(R.drawable.glute_press1,R.drawable.glute_press2, "Glute Press", "Adjust the pad to the proper height. Grasp the handles with elbows in a straight line with the hands and under the shoulders. Set the weight to the appropriate level before beginning. With the knees on the lower pad, place one foot on the rear platform.\n\nPress the foot into the platform and straighten the leg. Slowly return to the starting positing. Repeat for the desired number of reps before switching feet."));

        // upper_legs
        upper_legs.add(new Exercise_Item(R.drawable.back_squat1,R.drawable.back_squat2, "Back Squat", "Place a barbell in a rack just below shoulder-height. Dip under the bar to put it behind the neck across the top of the back, and grip the bar with the hands wider than shoulder-width apart.\n\nLift the chest up and squeeze the shoulder blades together to keep the straight back throughout the entire movement. Stand up to bring the bar off the rack and step backwards, then place the feet so that they are a little wider than shoulder-width apart.\n\nSit back into hips and keep the back straight and the chest up, squatting down so the hips are below the knees. From the bottom of the squat, press feet into the ground and push hips forward to return to the top of the standing position."));
        upper_legs.add(new Exercise_Item(R.drawable.forward_lunge1,R.drawable.forward_lunge2, "Forward Lunge", "Place a barbell in a rack at approximately shoulder-height. Dip under the bar so that it rests behind the neck across the top of the back and shoulder blades, and grip the bar with the hands a little wider than shoulder-width apart.\n\nLift the chest and squeeze the shoulder blades together to keep the back straight through the entire movement. Stand up to bring the bar off the rack and take 2 steps backwards. Place both feet so that they are about hip-width apart, and step forward with the right leg.\n\nLower the left knee almost to the floor without touching the ground. From the bottom of the movement push the right foot into the floor and pull back with the left leg to stand up. Alternate the legs for the desired number of repetitions."));
        // cardio
        cardio.add(new Exercise_Item(R.drawable.asynchronous_waves1,R.drawable.asynchronous_waves2, "Asynchronous Waves", "Securely attach a heavy rope to an anchor point, grab one end of the rope in each hand with the palms down. Stand with the feet about shoulder-width apart with both the hips and knees slightly bent, and hold both lines of rope directly in front. Hold one end of the rope in each hand and keep the elbows close to the body.\n\nLift the right arm up quickly before snapping it down to create a wave down the rope. As the right arm comes down quickly, lift the left arm at the same time to start moving the left side of the rope. Alternate arms to explosively snap the rope up and down for the duration of the exercise."));
        cardio.add(new Exercise_Item(R.drawable.pushup1,R.drawable.pushup2, "Push-up", "Step 1: Starting Position: Kneel on an exercise mat or floor and bring your feet together behind you.\n"+"\n"+"Step 2: Slowly bend forward to place your palms flat on the mat, positioning your hands shoulder-width apart with your fingers facing forward or turned slightly inward. Slowly shift your weight forward until your shoulders are positioned directly over your hands. \n"+"\n"+"Dtep 3: Downward Phase: Slowly lower your body towards the floor while maintaining a rigid torso and head aligned with your spine. Do not allow your low back to sag or your hips to hike upwards during this downward phase. \n"+"\n"+"Step 4: Upward Phase: Press upwards through your arms while maintaining a rigid torso and head aligned with your spine. For extra strength think about pushing the floor away from you."));
        //---------------------------------------------------------------------------------------------------------------

        exercise_items.addAll(chest);
        exercise_items.addAll(bicep);
        exercise_items.addAll(tricep);
        exercise_items.addAll(shoulders);
        exercise_items.addAll(back);
        exercise_items.addAll(forearm);
        exercise_items.addAll(core);
        exercise_items.addAll(glutes);
        exercise_items.addAll(upper_legs);
        exercise_items.addAll(cardio);
        Collections.shuffle(exercise_items);


        // building recyclerview using adapter and passing exercise_item through it
        mRecyclerView = v.findViewById(R.id.recyclerView2);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new Exercise_Adapter(exercise_items);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return v;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.exercise_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.Bicep_menu:
                exercise_items.clear();
                exercise_items.addAll(bicep);
                update();
                return true;
            case R.id.Tricep_menu:
                exercise_items.clear();
                exercise_items.addAll(tricep);

                update();
                return true;

            case R.id.Shoulders_menu:
                exercise_items.clear();
                exercise_items.addAll(shoulders);

                update();
                return true;

            case R.id.Back_menu:
                exercise_items.clear();
                exercise_items.addAll(back);

                update();
                return true;

            case R.id.Chest_menu:
                exercise_items.clear();
                exercise_items.addAll(chest);
                update();
                return true;

            case R.id.Forearm_menu:
                exercise_items.clear();
                exercise_items.addAll(forearm);

                update();
                return true;

            case R.id.Core_menu:
                exercise_items.clear();
                exercise_items.addAll(core);

                update();
                return true;

            case R.id.Glutes_menu:
                exercise_items.clear();
                exercise_items.addAll(glutes);

                update();
                return true;

            case R.id.Upper_Legs_menu:
                exercise_items.clear();
                exercise_items.addAll(upper_legs);

                update();
                return true;

            case R.id.Cardio:
                exercise_items.clear();
                exercise_items.addAll(cardio);
                update();
                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }
}