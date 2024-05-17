

-- Variables for user details
DO $$
    DECLARE
        user_id UUID;
        holder_id UUID;
        policy_owner_id UUID;
        policy_owner_ids UUID[] := '{}';
        i INT;
        hashed_password TEXT := '6ca13d52ca70c883e0f0bb101e425a89e8624de51db2d2392593af6a84118090';
    BEGIN
        -- Insert policy owners and collect their IDs
        FOR i IN 1..10 LOOP
                INSERT INTO users (username, name, hash_password, phone, email, address, role_id, created_at)
                VALUES ('policy_owner_' || i, 'Policy Owner ' || i, hashed_password,
                        '123-456-7890', 'policy_owner_' || i || '@example.com', '123 Elm St', 13, now())
                RETURNING id INTO policy_owner_id;

                INSERT INTO customers (user_id, role_id)
                VALUES (policy_owner_id, 13);

                -- Add policy_owner_id to the array
                policy_owner_ids := array_append(policy_owner_ids, policy_owner_id);
            END LOOP;

        -- Insert policy holders and assign a random policy owner to each
        FOR i IN 1..30 LOOP
                policy_owner_id := policy_owner_ids[1 + floor(random() * array_length(policy_owner_ids, 1))];
                INSERT INTO users (username, name, hash_password, phone, email, address, role_id, created_at)
                VALUES ('policy_holder_' || i, 'Policy Holder ' || i, hashed_password,
                        '123-456-7890', 'policy_holder_' || i || '@example.com', '123 Elm St', 11, now())
                RETURNING id INTO holder_id;

                INSERT INTO customers (user_id, role_id, policy_owner_id)
                VALUES (holder_id, 11, policy_owner_id);
            END LOOP;

        -- Insert dependents and assign them to policy holders
        FOR i IN 1..50 LOOP
                holder_id := (SELECT id FROM users WHERE username = 'policy_holder_' || ((i % 30) + 1));
                policy_owner_id := (SELECT c.policy_owner_id FROM customers c WHERE c.user_id = holder_id);
                INSERT INTO users (username, name, hash_password, phone, email, address, role_id, created_at)
                VALUES ('dependent_' || i, 'Dependent ' || i, hashed_password,
                        '123-456-7890', 'dependent_' || i || '@example.com', '123 Elm St', 12, now())
                RETURNING id INTO user_id;

                INSERT INTO customers (user_id, role_id, policy_holder_id, policy_owner_id)
                VALUES (user_id, 12, holder_id, policy_owner_id);
            END LOOP;

        -- Insert managers
        FOR i IN 1..5 LOOP
                INSERT INTO users (username, name, hash_password, phone, email, address, role_id, created_at)
                VALUES ('manager_' || i, 'Manager ' || i, hashed_password,
                        '123-456-7890', 'manager_' || i || '@example.com', '123 Elm St', 21, now())
                RETURNING id INTO user_id;

            END LOOP;

        -- Insert surveyors
        FOR i IN 1..10 LOOP
                INSERT INTO users (username, name, hash_password, phone, email, address, role_id, created_at)
                VALUES ('surveyor_' || i, 'Surveyor ' || i, hashed_password,
                        '123-456-7890', 'surveyor_' || i || '@example.com', '123 Elm St', 22, now())
                RETURNING id INTO user_id;
            END LOOP;
    END $$;
